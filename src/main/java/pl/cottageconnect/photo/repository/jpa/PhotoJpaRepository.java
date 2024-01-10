package pl.cottageconnect.photo.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.cottageconnect.photo.entity.PhotoEntity;

public interface PhotoJpaRepository extends JpaRepository<PhotoEntity, Long> {
}
