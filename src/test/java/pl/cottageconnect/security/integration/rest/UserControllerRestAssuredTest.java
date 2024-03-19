package pl.cottageconnect.security.integration.rest;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import pl.cottageconnect.configuration.RestAssuredIntegrationTestBase;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;
import pl.cottageconnect.security.integration.support.AuthenticationControllerTestSupport;
import pl.cottageconnect.security.integration.support.UserControllerTestSupport;

import static pl.cottageconnect.util.TestDataFactoryUser.testChangePasswordRequest;
import static pl.cottageconnect.util.TestDataFactoryUser.testRegistrationRequestCustomer;

@TestPropertySource(properties = "test.name=AuthenticationControllerRestAssuredTest")
class UserControllerRestAssuredTest
        extends RestAssuredIntegrationTestBase
        implements UserControllerTestSupport, AuthenticationControllerTestSupport {


    @Test
    void shouldChangePasswordForUser() {
        //given
        RegistrationRequestDTO registrationRequestDTO = testRegistrationRequestCustomer();
        ChangePasswordRequestDTO changePasswordRequestDTO = testChangePasswordRequest();
        //when
        AuthenticationResponseDTO authenticationResponseDTO = registerUser(registrationRequestDTO);
        ValidatableResponse validatableResponse = changePassword(changePasswordRequestDTO,
                authenticationResponseDTO.getAccessToken());
        //then
        validatableResponse.statusCode(HttpStatus.ACCEPTED.value());
    }
}
