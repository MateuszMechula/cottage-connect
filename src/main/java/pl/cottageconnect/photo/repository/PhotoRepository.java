package pl.cottageconnect.photo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.photo.domain.Photo;
import pl.cottageconnect.photo.entity.PhotoEntity;
import pl.cottageconnect.photo.repository.jpa.PhotoJpaRepository;
import pl.cottageconnect.photo.repository.mapper.PhotoEntityMapper;
import pl.cottageconnect.photo.service.dao.PhotoDAO;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PhotoRepository implements PhotoDAO {

    private final PhotoJpaRepository photoJpaRepository;
    private final PhotoEntityMapper photoEntityMapper;

    @Override
    public Optional<Photo> findPhotoById(Long photoId) {
        return photoJpaRepository.findById(photoId)
                .map(photoEntityMapper::mapFromEntity);
    }

    @Override
    public void addPhoto(Photo photo) {
        PhotoEntity photoToSave = photoEntityMapper.mapToEntity(photo);
        photoJpaRepository.save(photoToSave);
    }

    @Override
    public void deleteById(Long photoId) {
        photoJpaRepository.deleteById(photoId);
    }
}
