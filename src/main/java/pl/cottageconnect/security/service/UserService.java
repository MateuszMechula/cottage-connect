package pl.cottageconnect.security.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.cottageconnect.security.controller.dto.AuthenticationRequest;
import pl.cottageconnect.security.controller.dto.AuthenticationResponse;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequest;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.RoleEntity;
import pl.cottageconnect.security.enums.RoleEnum;
import pl.cottageconnect.security.service.dao.UserDAO;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(User user, RoleEnum role) {
        User toSave = buildUser(user, role);
        String email = user.getEmail();
        userDAO.save(toSave);
        var jwtToken = jwtService.generateToken(email);
        var refreshToken = jwtService.createRefreshToken(email);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        if (authentication.isAuthenticated()) {
            var jwtToken = jwtService.generateToken(request.getEmail());
            var refreshToken = jwtService.createRefreshToken(request.getEmail());

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var email = connectedUser.getName();
        User user = userDAO.findByEmail(email).orElseThrow();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        User toSave = user.withPassword(passwordEncoder.encode(request.getNewPassword()));

        userDAO.save(toSave);
    }

    private User buildUser(User user, RoleEnum role) {
        return User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(assignRoles(role.toString()))
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
