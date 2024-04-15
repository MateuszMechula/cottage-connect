package pl.cottageconnect.photo;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.cottageconnect.common.exception.exceptions.FileStorageException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static pl.cottageconnect.photo.FileStorageService.ErrorMessages.*;

@Service
@AllArgsConstructor
class FileStorageService {
    private static final String UPLOAD_FOLDER = "src/main/resources/static/images";

    public Resource loadImageAsResource(String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_FOLDER, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException(FAILED_TO_LOAD + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileStorageException(INVALID_PATH + filename, e);
        }
    }

    public String saveImageToFileSystem(MultipartFile imageFile) throws IOException {
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_FOLDER, fileName);

        Files.createDirectories(filePath.getParent());
        try (OutputStream os = Files.newOutputStream(filePath)) {
            os.write(imageFile.getBytes());
        }

        return fileName;
    }

    public void deleteImageFromFileSystem(String photoPath) throws IOException {
        Path filePath = Paths.get(UPLOAD_FOLDER, photoPath);

        if (fileExists(filePath)) {
            fileDelete(filePath);
        } else {
            throw new FileNotFoundException(FILE_NOT_FOUND.formatted(filePath));
        }
    }

    private boolean fileExists(Path path) {
        return Files.exists(path);
    }

    private void fileDelete(Path path) throws IOException {
        Files.delete(path);
    }

    static final class ErrorMessages {
        static final String FILE_NOT_FOUND = "File not found: [%s]";
        static final String FAILED_TO_LOAD = "Failed to load file: ";
        static final String INVALID_PATH = "Invalid file path: ";
    }
}
