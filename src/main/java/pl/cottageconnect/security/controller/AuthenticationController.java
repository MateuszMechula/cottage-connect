package pl.cottageconnect.security.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.security.controller.dto.AccountDetailsDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;

import java.security.Principal;

import static pl.cottageconnect.security.controller.AuthenticationController.Routes.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "auth", description = "Endpoints for user authentication")
public class AuthenticationController {

    private final UserService service;

    @Operation(
            summary = "Find all information about user",
            description = "Retrieve the complete account details for the currently authenticated user"
    )
    @GetMapping(value = ACCOUNT_DETAILS)
    public ResponseEntity<AccountDetailsDTO> getAccountDetails(
            Principal connectedUser) {

        AccountDetailsDTO userDetails = service.getUserDetails(connectedUser);
        return ResponseEntity.status(HttpStatus.OK).body(userDetails);
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the specified role."
    )
    @PostMapping(value = REGISTER_PATH)
    public ResponseEntity<AuthenticationResponseDTO> registerUser(
            @RequestBody @Valid RegistrationRequestDTO registrationRequestDTO) {

        AuthenticationResponseDTO authenticationResponseDTO = service.register(registrationRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponseDTO);
    }

    @Operation(
            summary = "Authenticate user and get token",
            description = "Authenticates a user and returns an authentication token."
    )
    @PostMapping(value = AUTHENTICATE_PATH)
    public AuthenticationResponseDTO authenticateAndGetToken(
            @RequestBody AuthenticationRequestDTO authenticationRequestDTO) {

        return service.authenticate(authenticationRequestDTO);
    }

    public static final class Routes {
        public static final String BASE_PATH = "/api/v1/auth";
        public static final String REGISTER_PATH = BASE_PATH + "/register";
        public static final String AUTHENTICATE_PATH = BASE_PATH + "/authenticate";
        public static final String ACCOUNT_DETAILS = BASE_PATH + "/account/details";
    }
}

