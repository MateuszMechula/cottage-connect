package pl.cottageconnect.photo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.cottageconnect.common.exception.exceptions.FileStorageException;
import pl.cottageconnect.common.exception.exceptions.MaxPhotoCountExceededException;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.cottage.CottageService;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.village.VillageService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
    private static final String PHOTO_URL = "http://localhost:8080/images/";

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
    @Transactional
    public List<String> getPhotosByPhotoableId(Long photoableId, PhotoableType type, Principal connectedUser) {
        validatePhotoable(photoableId, type, connectedUser);

        List<Photo> photos = photoDAO.findPhotoByPhotoableId(photoableId);
        if (photos.isEmpty()) {
            throw new NotFoundException(PHOTO_BY_PHOTOABLE_ID_NOT_FOUND.formatted(photoableId));
        }

        List<Resource> resourceList = photos.stream()
                .map(photo -> fileStorageService.loadImageAsResource(photo.path()))
                .toList();
        return resourceList.stream()
                .map(convertPhotoUrl())
                .toList();

    }

    @Override
    @Transactional
    public void uploadPhoto(Long photoableId, PhotoableType type, Principal connectedUser, MultipartFile file)
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

    private Function<Resource, String> convertPhotoUrl() {
        return resource -> {
            try {
                String filename = Paths.get(resource.getURL().toURI()).getFileName().toString();
                return PHOTO_URL + filename;
            } catch (IOException | URISyntaxException e) {
                throw new FileStorageException(GENERATE_URL_FAILED, e);
            }
        };
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
        static final String PHOTO_BY_PHOTOABLE_ID_NOT_FOUND = "Photo by photoable ID: [%s] not found or you dont have access";
        static final String MAX_PHOTO_LIMIT = "Max photo limit exceeded for type: ";
        static final String GENERATE_URL_FAILED = "Failed to generate URL for photo";
    }
}
