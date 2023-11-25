package pl.cottageconnect.integration.rest;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.cottageconnect.integration.configuration.RestAssuredIntegrationTestBase;
import pl.cottageconnect.integration.support.AuthenticationControllerTestSupport;
import pl.cottageconnect.integration.support.UserControllerTestSupport;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;

import static pl.cottageconnect.util.TestDataFactoryUser.testChangePasswordRequest;
import static pl.cottageconnect.util.TestDataFactoryUser.testRegistrationRequest;

public class UserControllerRestAssuredTest
        extends RestAssuredIntegrationTestBase
        implements UserControllerTestSupport, AuthenticationControllerTestSupport {

    @Test
    void shouldChangePasswordForUser() {
        //given
        RegistrationRequestDTO registrationRequestDTO = testRegistrationRequest();
        ChangePasswordRequestDTO changePasswordRequestDTO = testChangePasswordRequest();
        //when
        AuthenticationResponseDTO authenticationResponseDTO = registerUser(registrationRequestDTO);
        ValidatableResponse validatableResponse = changePassword(changePasswordRequestDTO, authenticationResponseDTO.getAccessToken());
        //then
        validatableResponse.statusCode(HttpStatus.ACCEPTED.value());
    }
}
