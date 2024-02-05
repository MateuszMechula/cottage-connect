package pl.cottageconnect.util;

import pl.cottageconnect.photo.domain.Photo;
import pl.cottageconnect.photo.entity.PhotoEntity;
import pl.cottageconnect.photo.enums.PhotoableType;

public class TestDataFactoryPhoto {

    public static Photo testPhoto() {
        return Photo.builder()
                .photoId(1L)
                .type(PhotoableType.COTTAGE)
                .photoableId(1L)
                .path("path")
                .build();
    }

    public static PhotoEntity testPhotoEntity() {
        return PhotoEntity.builder()
                .photoId(1L)
                .type(PhotoableType.COTTAGE)
                .photoableId(1L)
                .path("path")
                .build();
    }
}
