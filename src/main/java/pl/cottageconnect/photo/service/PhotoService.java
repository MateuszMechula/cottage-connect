package pl.cottageconnect.photo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.cottage.service.CottageService;
import pl.cottageconnect.photo.domain.Photo;
import pl.cottageconnect.photo.enums.PhotoableType;
import pl.cottageconnect.photo.service.dao.PhotoDAO;
import pl.cottageconnect.security.service.UserService;

import java.io.IOException;
import java.security.Principal;

import static pl.cottageconnect.photo.service.PhotoService.ErrorMessages.PHOTO_NOT_FOUND;
import static pl.cottageconnect.photo.service.PhotoService.ErrorMessages.UNSUPPORTED_PHOTOABLE_TYPE;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoDAO photoDAO;
    private final UserService userService;
    private final CottageService cottageService;
    private final FileStorageService fileStorageService;

    @Transactional
    public Photo getPhotoById(Long photoId) {
        return photoDAO.findPhotoById(photoId)
                .orElseThrow(() -> new NotFoundException(PHOTO_NOT_FOUND.formatted(photoId)));
    }

    @Transactional
    public void addPhoto(Long photoableId, PhotoableType type, Principal connectedUser, MultipartFile file)
            throws IOException {

        validatePhotoable(photoableId, type, connectedUser);
        String fileName = fileStorageService.saveImageToFileSystem(file);
        Photo photoToSave = buildPhoto(photoableId, type, fileName);

        photoDAO.addPhoto(photoToSave);
    }

    @Transactional
    public void deletePhoto(Long photoId, Principal connectedUser) throws IOException {
        Photo photoToDelete = getPhotoById(photoId);
        Long photoableId = photoToDelete.getPhotoableId();
        PhotoableType type = photoToDelete.getType();
        validatePhotoable(photoableId, type, connectedUser);

        String photoPath = photoToDelete.getPath();
        fileStorageService.deleteImageFromFileSystem(photoPath);

        photoDAO.deleteById(photoId);
    }

    private void validatePhotoable(Long photoableId, PhotoableType type, Principal connectedUser) {
        switch (type) {
            case USER -> userService.getUser(Math.toIntExact(photoableId), connectedUser);
            case COTTAGE -> cottageService.getCottageWithCheck(photoableId, connectedUser);
            default -> throw new IllegalArgumentException(UNSUPPORTED_PHOTOABLE_TYPE.formatted(photoableId));
        }
    }

    private Photo buildPhoto(Long photoableId, PhotoableType type, String photoPath) {
        return Photo.builder()
                .type(type)
                .photoableId(photoableId)
                .path(photoPath)
                .build();
    }

    static final class ErrorMessages {
        static final String UNSUPPORTED_PHOTOABLE_TYPE = "Unsupported photoable Type: [%s]";
        static final String PHOTO_NOT_FOUND = "Photo with ID: [%s] not found or you dont have access";
    }
}
