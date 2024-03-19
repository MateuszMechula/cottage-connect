package pl.cottageconnect.photo;

import java.util.Optional;

interface PhotoDAO {
    Optional<Photo> findPhotoById(Long photoId);

    void addPhoto(Photo photoToSave);

    void deleteById(Long photoId);
}
