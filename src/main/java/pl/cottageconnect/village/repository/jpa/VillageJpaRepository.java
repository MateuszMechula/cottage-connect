package pl.cottageconnect.village.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.village.entity.VillageEntity;

@Repository
public interface VillageJpaRepository extends JpaRepository<VillageEntity, Long> {
}
