package pl.cottageconnect.security.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class AuthenticationController {

    private final UserService service;
    private final UserMapperDTO userMapperDTO;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody @Valid RegistrationRequest registrationRequest,
            @RequestParam RoleEnum role) {

        User user = userMapperDTO.mapToUser(registrationRequest, role);
        AuthenticationResponse authenticationResponse = service.register(user, role);

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticateAndGetToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return service.authenticate(authenticationRequest);
    }

    @GetMapping("/user/ownerProfile")
    @PreAuthorize("hasAuthority('OWNER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/user/customerProfile")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String adminProfile() {
        return "Welcome to customer Profile";
    }
}

