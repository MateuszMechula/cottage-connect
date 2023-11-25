package pl.cottageconnect.security.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.cottageconnect.security.configuration.JwtAuthFilter;
import pl.cottageconnect.security.controller.UserController;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.util.TestDataFactoryUser;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.cottageconnect.security.controller.UserController.BASE_PATH;

@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class UserControllerWebMvcTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    void shouldChangePasswordSuccessfully() throws Exception {
        //given
        ChangePasswordRequestDTO request = TestDataFactoryUser.testChangePasswordRequest();
        String requestBody = objectMapper.writeValueAsString(request);
        Principal principal = mock(Principal.class);
        //when
        mockMvc.perform(patch(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .principal(principal))
                .andExpect(status().isAccepted());
        //then
        verify(userService, times(1)).changePassword(eq(request), any(Principal.class));
    }
}