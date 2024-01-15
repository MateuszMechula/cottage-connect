package pl.cottageconnect.owner.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.owner.entity.OwnerEntity;

import java.util.Optional;

@Repository
public interface OwnerJpaRepository extends JpaRepository<OwnerEntity, Long> {

    @Query("SELECT o FROM OwnerEntity o WHERE o.userId = :userId")
    Optional<OwnerEntity> findOwnerByUserId(@Param("userId") Integer userId);
}
