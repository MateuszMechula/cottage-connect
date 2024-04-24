package pl.cottageconnect.photo.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import pl.cottageconnect.photo.PhotoService;
import pl.cottageconnect.photo.PhotoableType;
import pl.cottageconnect.security.JwtAuthFilter;

import java.security.Principal;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.cottageconnect.photo.controller.PhotoController.Routes.ADD_PHOTO;
import static pl.cottageconnect.photo.controller.PhotoController.Routes.DELETE_PHOTO;

@WebMvcTest(PhotoController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PhotoControllerTest {
    private final MockMvc mockMvc;
    @MockBean
    private PhotoService photoService;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    void shouldAddPhotoSuccessfully() throws Exception {
        //given
        Long photoableId = 1L;
        PhotoableType type = PhotoableType.COTTAGE;
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());
        Principal connectedUser = mock(Principal.class);

        doNothing().when(photoService).uploadPhoto(photoableId, type, connectedUser, file);
        //when,then
        mockMvc.perform(multipart(ADD_PHOTO, photoableId)
                        .file(file)
                        .param("type", type.toString())
                        .principal(connectedUser))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeletePhotoSuccessfully() throws Exception {
        //given
        Long photoId = 1L;
        Principal connectedUser = mock(Principal.class);

        doNothing().when(photoService).deletePhoto(photoId, connectedUser);
        //when, then
        mockMvc.perform(delete(DELETE_PHOTO, photoId)
                        .principal(connectedUser))
                .andExpect(status().isNoContent());
    }
}