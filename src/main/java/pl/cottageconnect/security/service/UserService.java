package pl.cottageconnect.security.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.cottageconnect.security.configuration.JwtService;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.RoleEntity;
import pl.cottageconnect.security.enums.RoleEnum;
import pl.cottageconnect.security.exception.EmailAlreadyExistsException;
import pl.cottageconnect.security.exception.InvalidPasswordException;
import pl.cottageconnect.security.exception.PasswordMismatchException;
import pl.cottageconnect.security.service.dao.UserDAO;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponseDTO register(RegistrationRequestDTO request) {

        Optional<User> existingUser = userDAO.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException
                    ("Email address: [%s] already exists. Please use a different email."
                            .formatted(request.getEmail()));
        }
        User toSave = buildUser(request);
        String email = toSave.getEmail();
        userDAO.save(toSave);
        var jwtToken = jwtService.generateToken(email);
        var refreshToken = jwtService.createRefreshToken(email);

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        if (authentication.isAuthenticated()) {
            var jwtToken = jwtService.generateToken(request.getEmail());
            var refreshToken = jwtService.createRefreshToken(request.getEmail());

            return AuthenticationResponseDTO.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @Transactional
    public void changePassword(ChangePasswordRequestDTO request, Principal connectedUser) {
        var email = connectedUser.getName();
        User user = userDAO.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException
                        ("User email: [%s] not found".formatted(email)));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordMismatchException("Password are not the same");
        }
        User toSave = user.withPassword(passwordEncoder.encode(request.getNewPassword()));

        userDAO.save(toSave);
    }

    private User buildUser(RegistrationRequestDTO request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(assignRoles(request.getRole()))
                .build();
    }

    private Set<RoleEntity> assignRoles(String role) {
        if (role.equalsIgnoreCase(RoleEnum.OWNER.toString())) {
            return Set.of(RoleEntity.builder()
                    .roleId(1)
                    .role(RoleEnum.OWNER.toString())
                    .build());
        } else if (role.equalsIgnoreCase(RoleEnum.CUSTOMER.toString())) {
            return Set.of(RoleEntity.builder()
                    .roleId(2)
                    .role(RoleEnum.CUSTOMER.toString())
                    .build());
        }
        return Collections.emptySet();
    }
}
