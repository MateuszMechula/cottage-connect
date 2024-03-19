package pl.cottageconnect.photo;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.util.TestDataFactoryPhoto.testPhoto;
import static pl.cottageconnect.util.TestDataFactoryPhoto.testPhotoEntity;

class PhotoEntityMapperTest {
    private final PhotoEntityMapper photoEntityMapper = Mappers.getMapper(PhotoEntityMapper.class);

    @Test
    void shouldMapPhotoToPhotoEntitySuccessfully() {
        //given
        Photo photo = testPhoto();
        //when
        PhotoEntity photoEntity = photoEntityMapper.mapToEntity(photo);
        //then
        assertEquals(photo.photoId(), photoEntity.getPhotoId());
        assertEquals(photo.type(), photoEntity.getType());
        assertEquals(photo.photoableId(), photoEntity.getPhotoableId());
        assertEquals(photo.path(), photoEntity.getPath());
    }

    @Test
    void shouldMapPhotoEntityToPhotoSuccessfully() {
        //given
        PhotoEntity photoEntity = testPhotoEntity();
        //when
        Photo photo = photoEntityMapper.mapFromEntity(photoEntity);
        //then
        assertEquals(photoEntity.getPhotoId(), photo.photoId());
        assertEquals(photoEntity.getType(), photo.type());
        assertEquals(photoEntity.getPhotoableId(), photo.photoableId());
        assertEquals(photoEntity.getPath(), photo.path());
    }
}