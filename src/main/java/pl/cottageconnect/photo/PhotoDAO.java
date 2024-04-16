package pl.cottageconnect.photo;

import java.util.Optional;

interface PhotoDAO {
    Optional<Photo> findPhotoById(Long photoId);

    Optional<Photo> findPhotoByUserId(Integer integer);

    Long countByPhotoableIdAndType(Long photoableId, PhotoableType type);

    void addPhoto(Photo photoToSave);

    void deleteById(Long photoId);
}
