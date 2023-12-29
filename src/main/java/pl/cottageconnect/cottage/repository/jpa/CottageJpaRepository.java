package pl.cottageconnect.cottage.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.cottage.entity.CottageEntity;

@Repository
public interface CottageJpaRepository extends JpaRepository<CottageEntity, Long> {
}
