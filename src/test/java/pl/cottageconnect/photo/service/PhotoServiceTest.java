package pl.cottageconnect.photo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.cottage.service.CottageService;
import pl.cottageconnect.photo.domain.Photo;
import pl.cottageconnect.photo.enums.PhotoableType;
import pl.cottageconnect.photo.service.dao.PhotoDAO;
import pl.cottageconnect.security.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryPhoto.testPhoto;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {
    @Mock
    private PhotoDAO photoDAO;
    @Mock
    private UserService userService;
    @Mock
    private CottageService cottageService;
    @Mock
    private FileStorageService fileStorageService;
    @InjectMocks
    private PhotoService photoService;

    @Test
    void shouldGetPhotoByIdSuccessfully() {
        //given
        Long photoId = 1L;
        Photo photo = testPhoto();

        when(photoDAO.findPhotoById(photoId)).thenReturn(Optional.ofNullable(photo));
        //when
        Photo photoFoundById = photoService.getPhotoById(photoId);
        //then
        assertEquals(photo, photoFoundById);
    }

    @Test
    void shouldThrowExceptionWhenGetPhotoById() {
        //given
        Long photoId = 1L;

        when(photoDAO.findPhotoById(photoId)).thenReturn(Optional.empty());
        //when,then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> photoService.getPhotoById(photoId));
        verify(photoDAO, times(1)).findPhotoById(photoId);
        assertEquals(PhotoService.ErrorMessages.PHOTO_NOT_FOUND.formatted(photoId), exception.getMessage());
    }

    @Test
    void shouldAddPhotoSuccessfullyWithCottageCheck() throws IOException {
        //given
        Long photoableId = 1L;
        PhotoableType type = PhotoableType.COTTAGE;
        Principal connectedUser = Mockito.mock(Principal.class);
        MockMultipartFile file = new MockMultipartFile("file", "testFile.jpg", MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes());
        String fileName = file.getName();

        when(fileStorageService.saveImageToFileSystem(file)).thenReturn(fileName);
        //when
        photoService.addPhoto(photoableId, type, connectedUser, file);
        //then
        verify(photoDAO, times(1)).addPhoto(any(Photo.class));
        verify(cottageService, times(1)).getCottageWithCheck(photoableId, connectedUser);
    }

    @Test
    void shouldAddPhotoSuccessfullyWithUserCheck() throws IOException {
        //given
        Long photoableId = 1L;
        PhotoableType type = PhotoableType.USER;
        Principal connectedUser = Mockito.mock(Principal.class);
        MockMultipartFile file = new MockMultipartFile("file", "testFile.jpg", MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes());
        String fileName = file.getName();

        when(fileStorageService.saveImageToFileSystem(file)).thenReturn(fileName);
        //when
        photoService.addPhoto(photoableId, type, connectedUser, file);
        //then
        verify(photoDAO, times(1)).addPhoto(any(Photo.class));
        verify(userService, times(1)).getUser(Math.toIntExact(photoableId), connectedUser);

    }

    @Test
    void shouldThrowExceptionWhenAddPhoto() {
        //given
        Long photoableId = 1L;
        PhotoableType type = PhotoableType.NONE;
        Principal connectedUser = Mockito.mock(Principal.class);
        MultipartFile file = Mockito.mock(MultipartFile.class);

        //when,then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> photoService.addPhoto(photoableId, type, connectedUser, file));
        assertEquals(PhotoService.ErrorMessages.UNSUPPORTED_PHOTOABLE_TYPE.formatted(photoableId), exception.getMessage());

    }

    @Test
    void shouldDeletePhotoSuccessfully() throws IOException {
        //given
        Long photoId = 1L;
        Photo photo = testPhoto();
        String path = photo.getPath();
        Principal connectedUser = mock(Principal.class);

        when(photoDAO.findPhotoById(photoId)).thenReturn(Optional.of(photo));
        //when
        photoService.deletePhoto(photoId, connectedUser);
        //then
        verify(fileStorageService, times(1)).deleteImageFromFileSystem(path);
        verify(photoDAO, times(1)).deleteById(photoId);

    }
}