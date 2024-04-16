package pl.cottageconnect.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class PhotoRepository implements PhotoDAO {

    private final PhotoJpaRepository photoJpaRepository;
    private final PhotoEntityMapper photoEntityMapper;

    @Override
    public Optional<Photo> findPhotoById(Long photoId) {
        return photoJpaRepository.findById(photoId)
                .map(photoEntityMapper::mapFromEntity);
    }

    @Override
    public Optional<Photo> findPhotoByUserId(Integer userId) {
        return photoJpaRepository.findByPhotoableId(Long.valueOf(userId))
                .map(photoEntityMapper::mapFromEntity);
    }

    @Override
    public Long countByPhotoableIdAndType(Long photoableId, PhotoableType type) {
        return photoJpaRepository.countByPhotoableIdAndType(photoableId, type);
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
