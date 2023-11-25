package pl.cottageconnect.integration.support;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;

import static pl.cottageconnect.security.controller.UserController.BASE_PATH;

public interface UserControllerTestSupport {

    RequestSpecification requestSpecification();


    default ValidatableResponse changePassword(final ChangePasswordRequestDTO request, String token) {
        return requestSpecification()
                .body(request)
                .header("Authorization", "Bearer " + token)
                .when()
                .patch(BASE_PATH)
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());
    }

}
