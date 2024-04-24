package pl.cottageconnect.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.security.controller.dto.AccountDetailsDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;

import java.security.Principal;

import static pl.cottageconnect.security.controller.UserController.Routes.ACCOUNT_DETAILS;
import static pl.cottageconnect.security.controller.UserController.Routes.BASE_PATH;

@RestController
@RequiredArgsConstructor
@Tag(name = "user", description = "Endpoints for managing user-related operations")
@SecurityRequirement(name = "bearer-token")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Find all information about user",
            description = "Retrieve the complete account details for the currently authenticated user"
    )
    @GetMapping(value = ACCOUNT_DETAILS)
    public ResponseEntity<AccountDetailsDTO> getAccountDetails(
            Principal connectedUser) {

        AccountDetailsDTO userDetails = userService.getUserDetails(connectedUser);
        return ResponseEntity.status(HttpStatus.OK).body(userDetails);
    }

    @Operation(
            summary = "Change user password",
            description = "Allows users to change their password."
    )
    @PatchMapping(value = BASE_PATH)
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequestDTO request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }

    public static final class Routes {
        public static final String BASE_PATH = "/api/v1/users";
        public static final String ACCOUNT_DETAILS = BASE_PATH + "/account/details";
    }
}
