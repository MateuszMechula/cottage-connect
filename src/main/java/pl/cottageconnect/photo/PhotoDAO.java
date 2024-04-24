package pl.cottageconnect.photo;

import java.util.List;
import java.util.Optional;

interface PhotoDAO {
    Optional<Photo> findPhotoById(Long photoId);

    List<Photo> findPhotoByPhotoableId(Long photoableId);

    Long countByPhotoableIdAndType(Long photoableId, PhotoableType type);

    void addPhoto(Photo photoToSave);

    void deleteById(Long photoId);
}
