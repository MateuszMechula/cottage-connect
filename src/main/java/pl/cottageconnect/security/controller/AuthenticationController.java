package pl.cottageconnect.security.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cottageconnect.security.controller.dto.AuthenticationRequest;
import pl.cottageconnect.security.controller.dto.AuthenticationResponse;
import pl.cottageconnect.security.controller.dto.RegistrationRequest;
import pl.cottageconnect.security.controller.dto.mapper.UserMapperDTO;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.enums.RoleEnum;
import pl.cottageconnect.security.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "auth", description = "Endpoints for user authentication")
public class AuthenticationController {

    private final UserService service;
    private final UserMapperDTO userMapperDTO;

    @Operation(summary = "Register a new user", description = "Registers a new user with the specified role.")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody @Valid RegistrationRequest registrationRequest,
            @RequestParam RoleEnum role) {

        User user = userMapperDTO.mapToUser(registrationRequest, role);
        AuthenticationResponse authenticationResponse = service.register(user, role);

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
    }

    @Operation(summary = "Authenticate user and get token", description = "Authenticates a user and returns an authentication token.")
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticateAndGetToken(
            @RequestBody AuthenticationRequest authenticationRequest) {

        return service.authenticate(authenticationRequest);
    }
}

