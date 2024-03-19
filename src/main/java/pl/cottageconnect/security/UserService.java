package pl.cottageconnect.security;

import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;

import java.security.Principal;

public interface UserService {


    void getUserByUserId(Integer userId, Principal connectedUser);

    AuthenticationResponseDTO register(RegistrationRequestDTO request);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);

    void changePassword(ChangePasswordRequestDTO request, Principal connectedUser);

    void checkUserIdCompatibility(Integer userId, Principal connectedUser);

    User getUserByUsername(String email);

    User getConnectedUser(Principal connectedUser);
}
