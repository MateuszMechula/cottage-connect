package pl.cottageconnect.photo;

import java.util.Optional;

interface PhotoDAO {
    Optional<Photo> findPhotoById(Long photoId);

    Optional<Photo> findPhotoByUserId(Integer integer);

    void addPhoto(Photo photoToSave);

    void deleteById(Long photoId);
}
