package pl.cottageconnect.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.service.UserService;

import java.security.Principal;

import static pl.cottageconnect.security.controller.UserController.BASE_PATH;

@RestController
@RequestMapping(value = BASE_PATH)
@AllArgsConstructor
@Tag(name = "user", description = "Endpoints for managing user-related operations")
@SecurityRequirement(name = "bearer-token")
public class UserController {
    public static final String BASE_PATH = "/api/v1/users";
    private final UserService userService;

    @Operation(
            summary = "Change user password",
            description = "Allows users to change their password."
    )
    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequestDTO request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }
}
