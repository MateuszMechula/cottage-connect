package pl.cottageconnect.security.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;

import static pl.cottageconnect.security.controller.AuthenticationController.Routes.AUTHENTICATE_PATH;
import static pl.cottageconnect.security.controller.AuthenticationController.Routes.REGISTER_PATH;

@RestController
@RequiredArgsConstructor
@Tag(name = "auth", description = "Endpoints for user authentication")
public class AuthenticationController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the specified role."
    )
    @PostMapping(value = REGISTER_PATH)
    public ResponseEntity<AuthenticationResponseDTO> registerUser(
            @RequestBody @Valid RegistrationRequestDTO registrationRequestDTO) {

        AuthenticationResponseDTO authenticationResponseDTO = userService.register(registrationRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponseDTO);
    }

    @Operation(
            summary = "Authenticate user and get token",
            description = "Authenticates a user and returns an authentication token."
    )
    @PostMapping(value = AUTHENTICATE_PATH)
    public AuthenticationResponseDTO authenticateAndGetToken(
            @RequestBody AuthenticationRequestDTO authenticationRequestDTO) {

        return userService.authenticate(authenticationRequestDTO);
    }

    public static final class Routes {
        public static final String BASE_PATH = "/api/v1/auth";
        public static final String REGISTER_PATH = BASE_PATH + "/register";
        public static final String AUTHENTICATE_PATH = BASE_PATH + "/authenticate";
    }
}

