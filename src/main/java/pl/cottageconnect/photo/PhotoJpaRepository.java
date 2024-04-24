package pl.cottageconnect.photo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface PhotoJpaRepository extends JpaRepository<PhotoEntity, Long> {
    List<PhotoEntity> findByPhotoableId(Long userId);

    Long countByPhotoableIdAndType(Long photoableId, PhotoableType type);
}
