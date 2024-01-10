package pl.cottageconnect.photo.service.dao;

import pl.cottageconnect.photo.domain.Photo;

import java.util.Optional;

public interface PhotoDAO {
    Optional<Photo> findPhotoById(Long photoId);

    void addPhoto(Photo photoToSave);

    void deleteById(Long photoId);
}
