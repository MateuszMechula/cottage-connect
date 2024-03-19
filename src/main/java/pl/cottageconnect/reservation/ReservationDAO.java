package pl.cottageconnect.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

interface ReservationDAO {
    Optional<Reservation> findReservationById(Long reservationId);

    Page<Reservation> findReservationsByCustomerId(Long customerId, Pageable pageable);

    Optional<Page<Reservation>> findReservationsByCustomerIdAndStatus(Long customerId, Boolean status, Pageable pageable);

    Page<Reservation> findReservationsByCottageId(Long cottageId, Pageable pageable);

    Page<Reservation> findReservationsByCottageIdAndStatus(Long cottageId, Boolean status, Pageable pageable);

    void addReservationToCottage(Reservation reservation);

    void deleteReservation(Long reservationId);
}
