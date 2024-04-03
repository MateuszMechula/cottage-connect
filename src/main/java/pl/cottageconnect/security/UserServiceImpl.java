package pl.cottageconnect.security;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.customer.Customer;
import pl.cottageconnect.customer.CustomerService;
import pl.cottageconnect.owner.Owner;
import pl.cottageconnect.owner.OwnerService;
import pl.cottageconnect.security.controller.dto.*;
import pl.cottageconnect.security.exception.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static pl.cottageconnect.security.UserServiceImpl.ErrorMessages.*;

@Service
@AllArgsConstructor
class UserServiceImpl implements UserService {
    public static final Integer OWNER_ID = 1;
    public static final Integer CUSTOMER_ID = 2;

    private final UserDAO userDAO;
    private final OwnerService ownerService;
    private final CustomerService customerService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AccountDetailsDTO getUserDetails(Principal connectedUser) {
        User user = getConnectedUser(connectedUser);
        String role = user.roles().stream().findAny()
                .map(Role::role)
                .orElse(null);


        return Optional.ofNullable(role)
                .filter(r -> r.equals(RoleEnum.OWNER.toString()))
                .map(r -> ownerService.findOwnerByUserId(user.userId()))
                .map(owner -> createAccountDetailsDTO(user, owner, role))
                .orElseGet(() -> {
                    Customer customer = customerService.findCustomerByUserId(user.userId());
                    return createAccountDetailsDTO(user, customer, role);
                });
    }

    @Transactional
    public void getUserByUserId(Integer userId, Principal connectedUser) {
        User user = userDAO.getUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException(USER_WITH_ID_NOT_FOUND.formatted(userId)));

        checkUserIdCompatibility(user.userId(), connectedUser);
    }

    @Transactional
    public AuthenticationResponseDTO register(RegistrationRequestDTO request) {

        checkIfUserExistsByEmailAndThrow(request);

        User toSave = buildUser(request);
        String userEmail = toSave.email();

        User saved = userDAO.save(toSave);
        Integer userId = saved.userId();

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

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.password())) {
            throw new InvalidPasswordException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordMismatchException("Password are not the same");
        }
        User toSave = user.withPassword(passwordEncoder.encode(request.getNewPassword()));

        userDAO.save(toSave);
    }

    public void checkUserIdCompatibility(Integer userId, Principal connectedUser) {
        Integer expectedUserId = getConnectedUser(connectedUser).userId();
        if (!expectedUserId.equals(userId)) {
            throw new UserIdMismatchException(USER_ID_MISMATCH);
        }
    }

    public User getUserByUsername(String email) {
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException
                        (USER_EMAIL_NOT_FOUND.formatted(email)));
    }

    public User getConnectedUser(Principal connectedUser) {
        if (connectedUser == null) {
            throw new MissingUserException("Connected user is null");
        }
        String email = Objects.requireNonNull(connectedUser.getName());
        return getUserByUsername(email);
    }

    private AccountDetailsDTO createAccountDetailsDTO(User user, Person person, String role) {
        return AccountDetailsDTO.builder()
                .email(user.email())
                .role(role)
                .firstname(person.firstname())
                .lastname(person.lastname())
                .phone(person.phone())
                .build();
    }

    private void createRoleSpecificEntity(RegistrationRequestDTO request, Integer userId) {
        if (request.role().equals(RoleEnum.OWNER.toString())) {
            Owner owner = buildOwner(request, userId);
            ownerService.save(owner);

        } else if (request.role().equals(RoleEnum.CUSTOMER.toString())) {
            Customer customer = buildCustomer(request, userId);
            customerService.save(customer);
        } else {
            throw new InvalidRoleException(INVALID_ROLE_NAME.formatted(request.role()));
        }
    }

    private void checkIfUserExistsByEmailAndThrow(RegistrationRequestDTO request) {
        Optional<User> existingUser = userDAO.findByEmail(request.email());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException
                    (EMAIL_ALREADY_EXISTS.formatted(request.email()));
        }
    }

    private Customer buildCustomer(RegistrationRequestDTO request, Integer userId) {
        return Customer.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .phone(request.phone())
                .userId(userId)
                .build();
    }

    private Owner buildOwner(RegistrationRequestDTO request, Integer userId) {
        return Owner.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .phone(request.phone())
                .userId(userId)
                .build();
    }

    private User buildUser(RegistrationRequestDTO request) {
        return User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(assignRoles(request.role()))
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

    public static final class ErrorMessages {
        public static final String USER_WITH_ID_NOT_FOUND = "User with ID: [%s] not found or you dont have access";
        public static final String USER_ID_MISMATCH = "User ID Mismatch - The provided user IDs do not match.";
        public static final String USER_EMAIL_NOT_FOUND = "User email: [%s] not found";
        public static final String EMAIL_ALREADY_EXISTS = "Email address: [%s] already exists. Please use a different email.";
        public static final String INVALID_ROLE_NAME = "Invalid role name: [%s]";
    }
}
