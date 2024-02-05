package pl.cottageconnect.photo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static pl.cottageconnect.photo.service.FileStorageService.ErrorMessages.FILE_NOT_FOUND;

@Service
@AllArgsConstructor
public class FileStorageService {

    public String saveImageToFileSystem(MultipartFile imageFile) throws IOException {
        String uploadDir = "src/main/resources/static/images";
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.createDirectories(filePath.getParent());
        try (OutputStream os = Files.newOutputStream(filePath)) {
            os.write(imageFile.getBytes());
        }

        return fileName;
    }

    public void deleteImageFromFileSystem(String photoPath) throws IOException {
        String uploadDir = "src/main/resources/static/images";
        Path filePath = Paths.get(uploadDir, photoPath);

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
    }
}
