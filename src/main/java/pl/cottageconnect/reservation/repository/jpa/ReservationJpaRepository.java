package pl.cottageconnect.reservation.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.cottageconnect.reservation.entity.ReservationEntity;

import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("SELECT r FROM ReservationEntity r WHERE r.customer.customerId = :customerId")
    Page<ReservationEntity> getAllReservationsByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    @Query("SELECT r FROM ReservationEntity r WHERE r.customer.customerId = :customerId AND r.status = :status")
    Optional<Page<ReservationEntity>> getAllReservationsByCustomerIdAndStatus(@Param("customerId") Long customerId,
                                                                              @Param("status") Boolean status,
                                                                              Pageable pageable);

    @Query("SELECT r FROM ReservationEntity  r WHERE r.cottage.cottageId = :cottageId")
    Page<ReservationEntity> getAllReservationsByCottageId(@Param("cottageId") Long cottageId, Pageable pageable);

    @Query("SELECT r FROM ReservationEntity r WHERE r.cottage.cottageId = :cottageId AND r.status = :status")
    Page<ReservationEntity> getAllReservationsByCottageIdAndStatus(@Param("cottageId") Long cottageId,
                                                                   @Param("status") Boolean status,
                                                                   Pageable pageable);
}
