package pl.cottageconnect.security.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.service.dao.CustomerDAO;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.service.dao.OwnerDAO;
import pl.cottageconnect.security.configuration.JwtService;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;
import pl.cottageconnect.security.domain.Role;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.enums.RoleEnum;
import pl.cottageconnect.security.exception.*;
import pl.cottageconnect.security.service.dao.UserDAO;

import java.security.Principal;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    public static final Integer OWNER_ID = 1;
    public static final Integer CUSTOMER_ID = 2;

    private final UserDAO userDAO;
    private final OwnerDAO ownerDAO;
    private final CustomerDAO customerDAO;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponseDTO register(RegistrationRequestDTO request) {

        checkIfUserExistsByEmailAndThrow(request);

        User toSave = buildUser(request);
        String userEmail = toSave.getEmail();

        User saved = userDAO.save(toSave);
        Integer userId = saved.getUserId();

        createRoleSpecificEntity(request, userId);

        var jwtToken = jwtService.generateToken(userEmail);
        var refreshToken = jwtService.createRefreshToken(userEmail);

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
        User user = getUserByUsername(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordMismatchException("Password are not the same");
        }
        User toSave = user.withPassword(passwordEncoder.encode(request.getNewPassword()));

        userDAO.save(toSave);
    }

    public User getUserByUsername(String email) {
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException
                        ("User email: [%s] not found".formatted(email)));
    }

    public User getConnectedUser(Principal connectedUser) {
        if (connectedUser == null) {
            throw new MissingUserException("Connected user is null");
        }
        String email = Objects.requireNonNull(connectedUser.getName());
        return getUserByUsername(email);
    }

    private void createRoleSpecificEntity(RegistrationRequestDTO request, Integer userId) {
        if (request.getRole().equals("OWNER")) {
            Owner owner = buildOwner(request, userId);
            ownerDAO.save(owner);

        } else if (request.getRole().equals("CUSTOMER")) {
            Customer customer = buildCustomer(request, userId);
            customerDAO.save(customer.withUserId(userId));
        } else {
            throw new InvalidRoleException(
                    "Invalid role name: [%s]".formatted(request.getRole()));
        }
    }

    private void checkIfUserExistsByEmailAndThrow(RegistrationRequestDTO request) {
        Optional<User> existingUser = userDAO.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException
                    ("Email address: [%s] already exists. Please use a different email."
                            .formatted(request.getEmail()));
        }
    }

    private Customer buildCustomer(RegistrationRequestDTO request, Integer userId) {
        return Customer.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .userId(userId)
                .build();
    }

    private Owner buildOwner(RegistrationRequestDTO request, Integer userId) {
        return Owner.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .userId(userId)
                .build();
    }

    private User buildUser(RegistrationRequestDTO request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(assignRoles(request.getRole()))
                .build();
    }

    private Set<Role> assignRoles(String role) {
        if (role.equalsIgnoreCase(RoleEnum.OWNER.toString())) {
            return Set.of(Role.builder()
                    .roleId(OWNER_ID)
                    .role(RoleEnum.OWNER.toString())
                    .build());
        } else if (role.equalsIgnoreCase(RoleEnum.CUSTOMER.toString())) {
            return Set.of(Role.builder()
                    .roleId(CUSTOMER_ID)
                    .role(RoleEnum.CUSTOMER.toString())
                    .build());
        }
        return Collections.emptySet();
    }
}
