package pl.cottageconnect.photo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface PhotoJpaRepository extends JpaRepository<PhotoEntity, Long> {
    Optional<PhotoEntity> findByPhotoableId(Long userId);
}
