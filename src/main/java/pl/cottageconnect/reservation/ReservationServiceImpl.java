package pl.cottageconnect.reservation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.cottageconnect.common.exception.exceptions.DateValidationException;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.common.exception.exceptions.ReservationAlreadyExistsException;
import pl.cottageconnect.cottage.Cottage;
import pl.cottageconnect.cottage.CottageService;
import pl.cottageconnect.customer.Customer;
import pl.cottageconnect.customer.CustomerService;
import pl.cottageconnect.security.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static pl.cottageconnect.reservation.ReservationServiceImpl.ErrorMessages.*;

@Service
@RequiredArgsConstructor
class ReservationServiceImpl implements ReservationService {
    private final UserService userService;
    private final ReservationDAO reservationDAO;
    private final CottageService cottageServiceImpl;
    private final CustomerService customerService;

    @Override
    @Transactional
    public Reservation findReservationById(Long reservationId) {
        return reservationDAO.findReservationById(reservationId)
                .orElseThrow(() -> new NotFoundException(RESERVATION_NOT_FOUND.formatted(reservationId)));
    }

    @Override
    @Transactional
    public Page<Reservation> getAllReservationsByCustomerId(Long customerId, Principal connectedUser,
                                                            Pageable pageable) {
        Integer userId = userService.getConnectedUser(connectedUser).userId();
        customerService.findCustomerById(customerId);
        customerService.findCustomerByUserId(userId);

        return reservationDAO.findReservationsByCustomerId(customerId, pageable);
    }

    @Override
    @Transactional
    public Page<Reservation> getAllReservationsByCustomerIdAndStatus(Long customerId, Boolean status, Principal connectedUser,
                                                                     Pageable pageable) {
        Integer userId = userService.getConnectedUser(connectedUser).userId();
        customerService.findCustomerById(customerId);
        customerService.findCustomerByUserId(userId);

        return reservationDAO.findReservationsByCustomerIdAndStatus(customerId, status, pageable)
                .orElseThrow(() -> new NotFoundException(RESERVATIONS_WITH_CUSTOMER_ID_AND_STATUS_NOT_FOUND
                        .formatted(customerId, status)));
    }

    @Override
    @Transactional
    public Page<Reservation> getAllReservationsByCottageId(Long cottageId, Principal connectedUser, Pageable pageable) {
        cottageServiceImpl.getCottageWithCheck(cottageId, connectedUser);

        return reservationDAO.findReservationsByCottageId(cottageId, pageable);
    }

    @Override
    @Transactional
    public Page<Reservation> getAllReservationsByCottageIdAndStatus(Long cottageId, Boolean status,
                                                                    Principal connectedUser, Pageable pageable) {
        cottageServiceImpl.getCottageWithCheck(cottageId, connectedUser);

        return reservationDAO.findReservationsByCottageIdAndStatus(cottageId, status, pageable);
    }

    @Override
    @Transactional
    public void addReservation(Long cottageId, LocalDate dayIn, LocalDate dayOut, Principal connectedUser) {
        validateDateRange(dayIn, dayOut);

        ZoneId currentZone = ZoneId.systemDefault();
        OffsetDateTime startDateTime = createStartDate(dayIn, currentZone);
        OffsetDateTime endDateTime = createEndDate(dayOut, currentZone);
        Cottage cottage = cottageServiceImpl.getCottageWithCheck(cottageId, connectedUser);
        checkCottageAvailability(cottage, startDateTime, endDateTime);
        Integer userId = userService.getConnectedUser(connectedUser).userId();
        Customer customer = customerService.findCustomerByUserId(userId);
        Reservation reservationToSave = createReservation(startDateTime, endDateTime, FALSE, customer,
                cottage);

        reservationDAO.addReservationToCottage(reservationToSave);
    }

    @Override
    @Transactional
    public void confirmReservationById(Long reservationId, Principal connectedUser) {
        Reservation resToConfirm = findReservationById(reservationId);
        Boolean status = resToConfirm.status();

        Long cottageId = resToConfirm.cottage().cottageId();
        cottageServiceImpl.getCottageWithCheck(cottageId, connectedUser);

        if (status.equals(FALSE)) {
            Reservation updatedReservation = createReservation(resToConfirm.dayIn(), resToConfirm.dayOut(), TRUE, resToConfirm.customer(),
                    resToConfirm.cottage());

            reservationDAO.addReservationToCottage(updatedReservation);
        }
    }

    @Override
    @Transactional
    public void deleteReservationById(Long reservationId, Principal connectedUSer) {
        Reservation reservation = findReservationById(reservationId);
        Integer expectedUserId = reservation.cottage().village().owner().userId();
        Integer userId = userService.getConnectedUser(connectedUSer).userId();
        if (expectedUserId.equals(userId)) {
            reservationDAO.deleteReservation(reservationId);
        }
    }

    private OffsetDateTime createEndDate(LocalDate dayOut, ZoneId currentZone) {
        return dayOut.atTime(11, 0).atZone(currentZone).toOffsetDateTime();
    }

    private OffsetDateTime createStartDate(LocalDate dayIn, ZoneId currentZone) {
        return dayIn.atTime(15, 0).atZone(currentZone).toOffsetDateTime();
    }

    private void validateDateRange(LocalDate dayIn, LocalDate dayOut) {
        boolean isBefore = dayIn.isAfter(dayOut) || dayIn.isBefore(LocalDate.now());
        if (isBefore) {
            throw new DateValidationException(DATE_VALIDATION_ERROR);
        }
    }

    private void checkCottageAvailability(Cottage cottage, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        boolean isCottageOccupied = cottage.reservations().stream()
                .anyMatch(res ->
                        (startDateTime.isAfter(res.dayIn()) && startDateTime.isBefore(res.dayOut()))
                                || (endDateTime.isAfter(res.dayIn()) && endDateTime.isBefore(res.dayOut()))
                                || (startDateTime.isEqual(res.dayIn()) || endDateTime.isEqual(res.dayOut())));

        if (isCottageOccupied) {
            throw new ReservationAlreadyExistsException(RESERVATION_ALREADY_EXISTS);
        }
    }

    private Reservation createReservation(OffsetDateTime startDateTime, OffsetDateTime endDateTime, Boolean status,
                                          Customer customer, Cottage cottage) {
        return Reservation.builder()
                .dayIn(startDateTime)
                .dayOut(endDateTime)
                .status(status)
                .customer(customer)
                .cottage(cottage)
                .build();
    }

    static final class ErrorMessages {
        static final String RESERVATION_NOT_FOUND = "Reservation with ID: [%s] not found";
        static final String RESERVATIONS_WITH_CUSTOMER_ID_AND_STATUS_NOT_FOUND = "Reservations with customer ID [%s] and " +
                "status [%s] not found";
        static final String RESERVATION_ALREADY_EXISTS = "Reservation already exists for the specified date range.";
        static final String DATE_VALIDATION_ERROR = "Date validation error: Start day cannot be: before the end day or" +
                "before today";
    }
}
