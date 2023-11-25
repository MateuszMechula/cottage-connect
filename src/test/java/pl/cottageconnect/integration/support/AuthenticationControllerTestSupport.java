package pl.cottageconnect.integration.support;

import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;

import static pl.cottageconnect.security.controller.AuthenticationController.*;

public interface AuthenticationControllerTestSupport {

    RequestSpecification requestSpecification();

    default AuthenticationResponseDTO registerUser(
            final RegistrationRequestDTO registrationRequestDTO) {

        return requestSpecification()
                .body(registrationRequestDTO)
                .post(BASE_PATH + REGISTER_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response()
                .as(AuthenticationResponseDTO.class);
    }

    default AuthenticationResponseDTO authenticateAndGetToken(
            final AuthenticationRequestDTO authenticationRequestDTO) {

        return requestSpecification()
                .body(authenticationRequestDTO)
                .post(BASE_PATH + AUTHENTICATE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .as(AuthenticationResponseDTO.class);
    }
}
