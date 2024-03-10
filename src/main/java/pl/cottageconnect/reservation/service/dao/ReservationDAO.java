package pl.cottageconnect.reservation.service.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.cottageconnect.reservation.domain.Reservation;

import java.util.Optional;

public interface ReservationDAO {
    Optional<Reservation> findReservationById(Long reservationId);

    Page<Reservation> findReservationsByCustomerId(Long customerId, Pageable pageable);

    Optional<Page<Reservation>> findReservationsByCustomerIdAndStatus(Long customerId, Boolean status, Pageable pageable);

    Page<Reservation> findReservationsByCottageId(Long cottageId, Pageable pageable);

    Page<Reservation> findReservationsByCottageIdAndStatus(Long cottageId, Boolean status, Pageable pageable);

    void addReservationToCottage(Reservation reservation);

    void deleteReservation(Long reservationId);
}
