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
import pl.cottageconnect.security.JwtAuthFilter;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.security.controller.AuthenticationController;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.cottageconnect.security.controller.AuthenticationController.Routes.AUTHENTICATE_PATH;
import static pl.cottageconnect.security.controller.AuthenticationController.Routes.REGISTER_PATH;
import static pl.cottageconnect.util.TestDataFactoryUser.*;

@WebMvcTest(value = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class AuthenticationControllerWebMvcTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        //given
        RegistrationRequestDTO request = testRegistrationRequestCustomer();
        AuthenticationResponseDTO response = testAuthenticationResponse();
        String requestBody = objectMapper.writeValueAsString(request);

        when(userService.register(request)).thenReturn(response);

        //when,then
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").value(TEST_ACCESS_TOKEN))
                .andExpect(jsonPath("$.refresh_token").value(TEST_REFRESH_TOKEN));
    }

    @Test
    void shouldAuthenticateAndGetTokenSuccessfully() throws Exception {
        //given
        AuthenticationRequestDTO request = testAuthenticationRequest();
        AuthenticationResponseDTO response = testAuthenticationResponse();

        String requestBody = objectMapper.writeValueAsString(request);

        when(userService.authenticate(request)).thenReturn(response);
        //when,then
        mockMvc.perform(post(AUTHENTICATE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(TEST_ACCESS_TOKEN))
                .andExpect(jsonPath("$.refresh_token").value(TEST_REFRESH_TOKEN));

    }
}