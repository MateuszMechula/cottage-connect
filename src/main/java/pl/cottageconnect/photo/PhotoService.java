package pl.cottageconnect.photo;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface PhotoService {
    Photo getPhotoById(Long photoId);

    void addPhoto(Long photoableId, PhotoableType type, Principal connectedUser, MultipartFile file) throws IOException;

    void deletePhoto(Long photoId, Principal connectedUser) throws IOException;

    Resource getPhotoByUserId(Principal connectedUser);
}
