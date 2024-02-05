package pl.cottageconnect.photo.repository.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.photo.domain.Photo;
import pl.cottageconnect.photo.entity.PhotoEntity;

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
        assertEquals(photo.getPhotoId(), photoEntity.getPhotoId());
        assertEquals(photo.getType(), photoEntity.getType());
        assertEquals(photo.getPhotoableId(), photoEntity.getPhotoableId());
        assertEquals(photo.getPath(), photoEntity.getPath());
    }

    @Test
    void shouldMapPhotoEntityToPhotoSuccessfully() {
        //given
        PhotoEntity photoEntity = testPhotoEntity();
        //when
        Photo photo = photoEntityMapper.mapFromEntity(photoEntity);
        //then
        assertEquals(photoEntity.getPhotoId(), photo.getPhotoId());
        assertEquals(photoEntity.getType(), photo.getType());
        assertEquals(photoEntity.getPhotoableId(), photo.getPhotoableId());
        assertEquals(photoEntity.getPath(), photo.getPath());
    }
}