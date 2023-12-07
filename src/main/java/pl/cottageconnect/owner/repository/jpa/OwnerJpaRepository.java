package pl.cottageconnect.owner.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.owner.entity.OwnerEntity;

@Repository
public interface OwnerJpaRepository extends JpaRepository<OwnerEntity, Long> {

    OwnerEntity findOwnerEntityByUserId(Integer userId);
}
