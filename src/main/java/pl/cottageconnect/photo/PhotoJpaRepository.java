package pl.cottageconnect.photo;

import org.springframework.data.jpa.repository.JpaRepository;

interface PhotoJpaRepository extends JpaRepository<PhotoEntity, Long> {
}
