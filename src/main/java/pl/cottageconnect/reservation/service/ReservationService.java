package pl.cottageconnect.reservation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.cottageconnect.common.exception.exceptions.DateValidationException;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.common.exception.exceptions.ReservationAlreadyExistsException;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.service.CottageService;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.service.CustomerService;
import pl.cottageconnect.reservation.domain.Reservation;
import pl.cottageconnect.reservation.service.dao.ReservationDAO;
import pl.cottageconnect.security.service.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static pl.cottageconnect.reservation.service.ReservationService.ErrorMessages.*;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final UserService userService;
    private final ReservationDAO reservationDAO;
    private final CottageService cottageService;
    private final CustomerService customerService;

    @Transactional
    public Reservation findReservationById(Long reservationId) {
        return reservationDAO.findReservationById(reservationId)
                .orElseThrow(() -> new NotFoundException(RESERVATION_NOT_FOUND.formatted(reservationId)));
    }

    @Transactional
    public Page<Reservation> getAllReservationsByCustomerId(Long customerId, Boolean status, Principal connectedUser,
                                                            Pageable pageable) {
        Integer userId = userService.getConnectedUser(connectedUser).getUserId();
        customerService.findCustomerById(customerId);
        customerService.findCustomerByUserId(userId);

        if (status != null) {
            return reservationDAO.findReservationsByCustomerIdAndStatus(customerId, status, pageable);
        } else {
            return reservationDAO.findReservationsByCustomerId(customerId, pageable);
        }
    }

    @Transactional
    public Page<Reservation> getAllReservationsByCottageId(Long cottageId, Boolean status,
                                                           Principal connectedUser, Pageable pageable) {
        cottageService.getCottageWithCheck(cottageId, connectedUser);

        if (status != null) {
            return reservationDAO.findReservationsByCottageIdAndStatus(cottageId, status, pageable);
        } else {
            return reservationDAO.findReservationsByCottageId(cottageId, pageable);
        }
    }


    @Transactional
    public void addReservation(Long cottageId, LocalDate dayIn, LocalDate dayOut, Principal connectedUser) {
        validateDateRange(dayIn, dayOut);

        ZoneId currentZone = ZoneId.systemDefault();
        OffsetDateTime startDateTime = createStartDate(dayIn, currentZone);
        OffsetDateTime endDateTime = createEndDate(dayOut, currentZone);
        Cottage cottage = cottageService.getCottageWithCheck(cottageId, connectedUser);
        checkCottageAvailability(cottage, startDateTime, endDateTime);
        Integer userId = userService.getConnectedUser(connectedUser).getUserId();
        Customer customer = customerService.findCustomerByUserId(userId);
        Reservation reservationToSave = createReservation(startDateTime, endDateTime, customer, cottage);

        reservationDAO.addReservationToCottage(reservationToSave);
    }

    @Transactional
    public void confirmReservationById(Long reservationId, Principal connectedUser) {
        Reservation reservationToConfirm = findReservationById(reservationId);
        Boolean status = reservationToConfirm.getStatus();

        Long cottageId = reservationToConfirm.getCottage().getCottageId();
        cottageService.getCottageWithCheck(cottageId, connectedUser);

        if (status.equals(FALSE)) {
            reservationDAO.addReservationToCottage(reservationToConfirm.withStatus(TRUE));
        }
    }

    @Transactional
    public void deleteReservationById(Long reservationId, Principal connectedUSer) {
        Reservation reservation = findReservationById(reservationId);
        Integer expectedUserId = reservation.getCottage().getVillage().getOwner().getUserId();
        Integer userId = userService.getConnectedUser(connectedUSer).getUserId();
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
        boolean isCottageOccupied = cottage.getReservations().stream()
                .anyMatch(res ->
                        (startDateTime.isAfter(res.getDayIn()) && startDateTime.isBefore(res.getDayOut()))
                                || (endDateTime.isAfter(res.getDayIn()) && endDateTime.isBefore(res.getDayOut()))
                                || (startDateTime.isEqual(res.getDayIn()) || endDateTime.isEqual(res.getDayOut())));

        if (isCottageOccupied) {
            throw new ReservationAlreadyExistsException(RESERVATION_ALREADY_EXISTS);
        }
    }

    private Reservation createReservation(OffsetDateTime startDateTime, OffsetDateTime endDateTime, Customer customer,
                                          Cottage cottage) {
        return Reservation.builder()
                .dayIn(startDateTime)
                .dayOut(endDateTime)
                .status(false)
                .customer(customer)
                .cottage(cottage)
                .build();
    }

    static final class ErrorMessages {
        static final String RESERVATION_NOT_FOUND = "Reservation with ID: [%s] not found";
        static final String RESERVATION_ALREADY_EXISTS = "Reservation already exists for the specified date range.";
        static final String DATE_VALIDATION_ERROR = "Date validation error: Start day cannot be: before the end day or" +
                "before today";
    }
}