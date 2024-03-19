package pl.cottageconnect.photo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryPhoto.testPhoto;
import static pl.cottageconnect.util.TestDataFactoryPhoto.testPhotoEntity;

@ExtendWith(MockitoExtension.class)
class PhotoRepositoryTest {
    @Mock
    private PhotoJpaRepository photoJpaRepository;
    @Mock
    private PhotoEntityMapper photoEntityMapper;
    @InjectMocks
    private PhotoRepository photoRepository;


    @Test
    void shouldFindPhotoByIdSuccessfully() {
        //given
        Long photoId = 1L;
        Photo photo = testPhoto();
        PhotoEntity photoEntity = testPhotoEntity();

        when(photoJpaRepository.findById(photoId)).thenReturn(Optional.ofNullable(photoEntity));
        when(photoEntityMapper.mapFromEntity(photoEntity)).thenReturn(photo);
        //when
        Optional<Photo> photoById = photoRepository.findPhotoById(photoId);
        //then
        assertTrue(photoById.isPresent());
        Photo photoFound = photoById.get();
        assertEquals(photo, photoFound);
    }

    @Test
    void shouldAddPhotoSuccessfully() {
        //given
        Photo photo = testPhoto();
        PhotoEntity photoEntity = testPhotoEntity();

        when(photoEntityMapper.mapToEntity(photo)).thenReturn(photoEntity);
        //when
        photoRepository.addPhoto(photo);
        //then
        verify(photoJpaRepository, times(1)).save(photoEntity);
    }

    @Test
    void shouldDeletePhotoByIdSuccessfully() {
        //given
        Long photoId = 1L;
        //when
        photoRepository.deleteById(photoId);
        //then
        verify(photoJpaRepository, times(1)).deleteById(photoId);
    }
}