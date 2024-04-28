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
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static pl.cottageconnect.photo.PhotoProperties.PHOTO_LIMITS;
import static pl.cottageconnect.photo.PhotoProperties.PHOTO_URL;
import static pl.cottageconnect.photo.PhotoServiceImpl.ErrorMessages.*;

@Service
@RequiredArgsConstructor
class PhotoServiceImpl implements PhotoService {

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
    public List<Photo> getPhotosByPhotoableId(Long photoableId, PhotoableType type, Principal connectedUser) {
        validatePhotoable(photoableId, type, connectedUser);

        List<Photo> photos = photoDAO.findPhotoByPhotoableId(photoableId);
        if (photos.isEmpty()) {
            throw new NotFoundException(PHOTO_BY_PHOTOABLE_ID_NOT_FOUND.formatted(photoableId));
        }
        return updatePhotosUrls(photos);
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

    private List<Photo> updatePhotosUrls(List<Photo> photos) {
        List<Photo> updatedPhotoList = new ArrayList<>();

        for (Photo photo : photos) {
            String path = photo.path();
            Resource resource = fileStorageService.loadImageAsResource(path);

            try {
                String filename = Paths.get(resource.getURI()).getFileName().toString();
                Photo updatedPhoto = Photo.builder()
                        .photoId(photo.photoId())
                        .type(photo.type())
                        .photoableId(photo.photoableId())
                        .path(PHOTO_URL + filename)
                        .build();
                updatedPhotoList.add(updatedPhoto);
            } catch (IOException e) {
                throw new FileStorageException(GENERATE_URL_FAILED, e);
            }
        }
        return updatedPhotoList;
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
