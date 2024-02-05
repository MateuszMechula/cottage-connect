package pl.cottageconnect.photo.integration;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.cottageconnect.photo.service.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AllArgsConstructor(onConstructor = @__(@Autowired))
class FileStorageServiceTest {
    private FileStorageService fileStorageService;

    @Test
    void shouldSaveImageToFileSystemSuccessfully() throws IOException {
        MultipartFile file = new MockMultipartFile("imageFile", "originalFilename.jpg",
                "image/jpg", "fakeImageBytes".getBytes());
        String returnedFileName;

        // When
        returnedFileName = fileStorageService.saveImageToFileSystem(file);

        // Then
        String uploadDir = "src/main/resources/static/images";
        Path expectedFilePath = Paths.get(uploadDir, returnedFileName);
        assertTrue(Files.exists(expectedFilePath));
        // Cleanup
        Files.deleteIfExists(expectedFilePath);
    }

    @Test
    void shouldDeleteImageFromFileSystemSuccessfully() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("imageFile", "originalFilename.jpg",
                "image/jpg", "fakeImageBytes".getBytes());
        String returnedFileName = fileStorageService.saveImageToFileSystem(file);

        // When
        fileStorageService.deleteImageFromFileSystem(returnedFileName);

        // Then
        String uploadDir = "src/main/resources/static/images";
        Path expectedFilePath = Paths.get(uploadDir, returnedFileName);
        assertFalse(Files.exists(expectedFilePath));
    }
}