package pl.cottageconnect.cottage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CottageJpaRepository extends JpaRepository<CottageEntity, Long> {
}
