package pl.cottageconnect.photo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.cottageconnect.photo.Photo;
import pl.cottageconnect.photo.PhotoService;
import pl.cottageconnect.photo.PhotoableType;
import pl.cottageconnect.photo.controller.dto.PhotoDTO;
import pl.cottageconnect.photo.controller.mapper.PhotoMapper;

import java.io.IOException;
import java.security.Principal;

import static pl.cottageconnect.photo.controller.PhotoController.Routes.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "photos", description = "Endpoints responsible for photos (<b>OWNER</b>, <b>CUSTOMER</b>). You can add photo " +
        "for types: USER, COTTAGE")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    @GetMapping(value = USER_PHOTO)
    public ResponseEntity<PhotoDTO> getUserPhoto(Principal connectedUser) {
        Photo photo = photoService.getPhotoByUserId(connectedUser);
        PhotoDTO photoDTO = photoMapper.mapToDTO(photo);
        return ResponseEntity.ok().body(photoDTO);
    }

    @Operation(
            summary = "Add Photo",
            description = "Endpoint to add a new photo for COTTAGE, USER")
    @PostMapping(value = ADD_PHOTO)
    public ResponseEntity<Void> addPhoto(@PathVariable Long photoableId,
                                         @RequestParam("type") PhotoableType type,
                                         @RequestParam("file") MultipartFile file,
                                         Principal connectedUser
    ) throws IOException {
        photoService.addPhoto(photoableId, type, connectedUser, file);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Delete Photo",
            description = "Endpoint to delete a photo by its unique ID")
    @DeleteMapping(value = DELETE_PHOTO)
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId, Principal connectedUser) throws IOException {
        photoService.deletePhoto(photoId, connectedUser);
        return ResponseEntity.noContent().build();
    }

    static final class Routes {
        static final String ROOT = "/api/v1/photos";
        static final String USER_PHOTO = "/api/v1/photos/user";
        static final String ADD_PHOTO = ROOT + "/{photoableId}";
        static final String DELETE_PHOTO = ROOT + "/{photoId}";
    }
}
