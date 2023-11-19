package pl.cottageconnect.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequest;
import pl.cottageconnect.security.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/users")
@AllArgsConstructor
@Tag(name = "user", description = "Endpoints for managing user-related operations")
@PreAuthorize("hasAnyAuthority('CUSTOMER', 'OWNER')")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Change user password", description = "Allows users to change their password.")
    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }
}
