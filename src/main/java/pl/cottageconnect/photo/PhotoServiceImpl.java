package pl.cottageconnect.photo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.cottageconnect.common.exception.exceptions.MaxPhotoCountExceededException;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.cottage.CottageService;
import pl.cottageconnect.security.User;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.village.VillageService;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import static pl.cottageconnect.photo.PhotoServiceImpl.ErrorMessages.*;

@Service
@RequiredArgsConstructor
class PhotoServiceImpl implements PhotoService {
    private static final Integer MAX_USER_PHOTO_VALUE = 1;
    private static final Integer MAX_VILLAGE_PHOTO_VALUE = 1;
    private static final Integer MAX_COTTAGE_PHOTO_VALUE = 5;
    private static final Map<PhotoableType, Integer> PHOTO_LIMITS = Map.of(
            PhotoableType.USER, MAX_USER_PHOTO_VALUE,
            PhotoableType.COTTAGE, MAX_COTTAGE_PHOTO_VALUE,
            PhotoableType.VILLAGE, MAX_VILLAGE_PHOTO_VALUE
    );

    private final PhotoDAO photoDAO;
    private final UserService userService;
    private final CottageService cottageService;
    private final VillageService villageService;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public Photo getPhotoById(Long photoId) {
        return photoDAO.findPhotoById(photoId)
                .orElseThrow(() -> new NotFoundException(PHOTO_NOT_FOUND.formatted(photoId)));
    }

    @Override
    public Resource getPhotoByUserId(Principal connectedUser) {
        User user = userService.getConnectedUser(connectedUser);
        Integer userId = user.userId();
        Photo photo = photoDAO.findPhotoByUserId(user.userId())
                .orElseThrow(() -> new NotFoundException(PHOTO_BY_USER_ID_NOT_FOUND.formatted(userId)));
        return fileStorageService.loadImageAsResource(photo.path());

    }

    @Override
    @Transactional
    public void addPhoto(Long photoableId, PhotoableType type, Principal connectedUser, MultipartFile file)
            throws IOException {

        validatePhotoable(photoableId, type, connectedUser);
        Long photoCount = photoDAO.countByPhotoableIdAndType(photoableId, type);
        checkPhotoLimit(type, photoCount);

        String fileName = fileStorageService.saveImageToFileSystem(file);
        Photo photoToSave = buildPhoto(photoableId, type, fileName);

        photoDAO.addPhoto(photoToSave);
    }

    @Override
    @Transactional
    public void deletePhoto(Long photoId, Principal connectedUser) throws IOException {
        Photo photoToDelete = getPhotoById(photoId);
        Long photoableId = photoToDelete.photoableId();
        PhotoableType type = photoToDelete.type();
        validatePhotoable(photoableId, type, connectedUser);

        String photoPath = photoToDelete.path();
        fileStorageService.deleteImageFromFileSystem(photoPath);

        photoDAO.deleteById(photoId);
    }

    private void checkPhotoLimit(PhotoableType type, Long photoCount) {
        if (PHOTO_LIMITS.getOrDefault(type, 0) <= photoCount) {
            throw new MaxPhotoCountExceededException(MAX_PHOTO_LIMIT + type);
        }
    }

    private void validatePhotoable(Long photoableId, PhotoableType type, Principal connectedUser) {
        switch (type) {
            case USER -> userService.getUserByUserId(Math.toIntExact(photoableId), connectedUser);
            case COTTAGE -> cottageService.getCottageWithCheck(photoableId, connectedUser);
            case VILLAGE -> villageService.checkVillageId(photoableId, connectedUser);
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
        static final String PHOTO_BY_USER_ID_NOT_FOUND = "Photo by user ID: [%s] not found or you dont have access";
        static final String MAX_PHOTO_LIMIT = "Max photo limit exceeded for type: ";
    }
}
