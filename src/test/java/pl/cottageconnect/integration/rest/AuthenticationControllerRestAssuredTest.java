package pl.cottageconnect.integration.rest;

import org.junit.jupiter.api.Test;
import pl.cottageconnect.integration.configuration.RestAssuredIntegrationTestBase;
import pl.cottageconnect.integration.support.AuthenticationControllerTestSupport;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.cottageconnect.util.TestDataFactoryUser.testAuthenticationRequest;
import static pl.cottageconnect.util.TestDataFactoryUser.testRegistrationRequestCustomer;

public class AuthenticationControllerRestAssuredTest
        extends RestAssuredIntegrationTestBase
        implements AuthenticationControllerTestSupport {

    @Test
    void shouldRegisterUserSuccessfully() {
        //given
        RegistrationRequestDTO registrationRequestDTO = testRegistrationRequestCustomer();
        //when
        AuthenticationResponseDTO authenticationResponseDTO = registerUser(registrationRequestDTO);
        //then
        assertThat(authenticationResponseDTO).isNotNull();
        assertThat(authenticationResponseDTO.getAccessToken()).isNotBlank();
        assertThat(authenticationResponseDTO.getRefreshToken()).isNotBlank();
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        //given
        AuthenticationRequestDTO authenticationRequestDTO = testAuthenticationRequest();
        RegistrationRequestDTO registrationRequestDTO = testRegistrationRequestCustomer();
        //when
        registerUser(registrationRequestDTO);
        AuthenticationResponseDTO authenticationResponseDTO = authenticateAndGetToken(authenticationRequestDTO);
        //then
        assertThat(authenticationResponseDTO).isNotNull();
        assertThat(authenticationResponseDTO.getAccessToken()).isNotBlank();
        assertThat(authenticationResponseDTO.getRefreshToken()).isNotBlank();
    }
}
