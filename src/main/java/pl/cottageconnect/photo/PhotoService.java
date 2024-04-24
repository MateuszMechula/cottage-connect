package pl.cottageconnect.photo;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface PhotoService {
    Photo getPhotoById(Long photoId);

    List<String> getPhotosByPhotoableId(Long photoableId, PhotoableType type, Principal connectedUser);

    void uploadPhoto(Long photoableId, PhotoableType type, Principal connectedUser, MultipartFile file) throws IOException;

    void deletePhoto(Long photoId, Principal connectedUser) throws IOException;
}
