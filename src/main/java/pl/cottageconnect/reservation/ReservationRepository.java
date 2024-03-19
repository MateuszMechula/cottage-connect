package pl.cottageconnect.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class ReservationRepository implements ReservationDAO {
    private final ReservationEntityMapper reservationEntityMapper;
    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public Optional<Reservation> findReservationById(Long reservationId) {
        return reservationJpaRepository.findById(reservationId)
                .map(reservationEntityMapper::mapFromEntity);
    }

    @Override
    public Page<Reservation> findReservationsByCustomerId(Long customerId, Pageable pageable) {
        return reservationJpaRepository.getAllReservationsByCustomerId(customerId, pageable)
                .map(reservationEntityMapper::mapFromEntity);
    }

    @Override
    public Optional<Page<Reservation>> findReservationsByCustomerIdAndStatus(Long customerId, Boolean status, Pageable pageable) {
        return reservationJpaRepository.getAllReservationsByCustomerIdAndStatus(customerId, status, pageable)
                .map(reservationPage -> reservationPage.map(reservationEntityMapper::mapFromEntity));
    }

    @Override
    public Page<Reservation> findReservationsByCottageId(Long cottageId, Pageable pageable) {
        return reservationJpaRepository.getAllReservationsByCottageId(cottageId, pageable)
                .map(reservationEntityMapper::mapFromEntity);
    }

    @Override
    public Page<Reservation> findReservationsByCottageIdAndStatus(Long cottageId, Boolean status, Pageable pageable) {
        return reservationJpaRepository.getAllReservationsByCottageIdAndStatus(cottageId, status, pageable)
                .map(reservationEntityMapper::mapFromEntity);
    }

    @Override
    public void addReservationToCottage(Reservation reservation) {
        ReservationEntity toSave = reservationEntityMapper.mapToEntity(reservation);
        reservationJpaRepository.save(toSave);
    }

    @Override
    public void deleteReservation(Long reservationId) {
        reservationJpaRepository.deleteById(reservationId);
    }
}
