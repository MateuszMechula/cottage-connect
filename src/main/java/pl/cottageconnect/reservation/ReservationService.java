package pl.cottageconnect.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.LocalDate;

public interface ReservationService {
    Reservation findReservationById(Long reservationId);

    Page<Reservation> getAllReservationsByCustomerId(Long customerId, Principal connectedUser,
                                                     Pageable pageable);

    Page<Reservation> getAllReservationsByCustomerIdAndStatus(Long customerId, Boolean status, Principal connectedUser,
                                                              Pageable pageable);

    Page<Reservation> getAllReservationsByCottageId(Long cottageId, Principal connectedUser, Pageable pageable);

    Page<Reservation> getAllReservationsByCottageIdAndStatus(Long cottageId, Boolean status,
                                                             Principal connectedUser, Pageable pageable);

    void addReservation(Long cottageId, LocalDate dayIn, LocalDate dayOut, Principal connectedUser);

    void confirmReservationById(Long reservationId, Principal connectedUser);

    void deleteReservationById(Long reservationId, Principal connectedUSer);
}
