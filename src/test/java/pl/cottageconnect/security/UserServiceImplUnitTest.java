package pl.cottageconnect.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.cottageconnect.customer.Customer;
import pl.cottageconnect.customer.CustomerService;
import pl.cottageconnect.owner.Owner;
import pl.cottageconnect.owner.OwnerService;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;
import pl.cottageconnect.security.exception.EmailAlreadyExistsException;
import pl.cottageconnect.security.exception.InvalidPasswordException;
import pl.cottageconnect.security.exception.PasswordMismatchException;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryUser.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {
    @Mock
    private UserDAO userDAO;
    @Mock
    private OwnerService ownerService;
    @Mock
    private CustomerService customerService;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void shouldRegisterUserSuccessfullyWithRoleCustomer() {
        //given
        User user = testUserRoleCustomer();
        Customer customer = testCustomer();
        RegistrationRequestDTO request = testRegistrationRequestCustomer();

        when(userDAO.findByEmail(user.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn(user.password());
        when(userDAO.save(user)).thenReturn(user);
        when(customerService.save(customer)).thenReturn(customer);
        when(jwtService.generateToken(anyString())).thenReturn(TEST_ACCESS_TOKEN);
        when(jwtService.createRefreshToken(anyString())).thenReturn(TEST_REFRESH_TOKEN);
        //when
        AuthenticationResponseDTO response = userServiceImpl.register(request);

        //then
        assertNotNull(response);
        assertEquals(TEST_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(TEST_REFRESH_TOKEN, response.getRefreshToken());
    }

    @Test
    void shouldRegisterUserSuccessfullyWithRoleOwner() {
        //given
        User user = testUserRoleOwner();
        Owner owner = testOwner();
        RegistrationRequestDTO request = testRegistrationRequestOwner();

        when(userDAO.findByEmail(user.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn(user.password());
        when(userDAO.save(user)).thenReturn(user);
        when(ownerService.save(owner)).thenReturn(owner);
        when(jwtService.generateToken(anyString())).thenReturn(TEST_ACCESS_TOKEN);
        when(jwtService.createRefreshToken(anyString())).thenReturn(TEST_REFRESH_TOKEN);
        //when
        AuthenticationResponseDTO response = userServiceImpl.register(request);

        //then
        assertNotNull(response);
        assertEquals(TEST_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(TEST_REFRESH_TOKEN, response.getRefreshToken());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        //given
        User user = testUserRoleCustomer();
        RegistrationRequestDTO request = testRegistrationRequestCustomer();

        when(userDAO.findByEmail(user.email())).thenReturn(Optional.of(user));

        //when, then
        assertThrows(EmailAlreadyExistsException.class, () -> userServiceImpl.register(request));
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        //given
        AuthenticationRequestDTO authenticationRequestDTO = testAuthenticationRequest();
        Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(jwtService.generateToken(TEST_USER_EMAIL)).thenReturn(TEST_ACCESS_TOKEN);
        when(jwtService.createRefreshToken(TEST_USER_EMAIL)).thenReturn(TEST_REFRESH_TOKEN);
        //when
        AuthenticationResponseDTO response = userServiceImpl.authenticate(authenticationRequestDTO);

        //then
        assertNotNull(response);
        assertEquals(TEST_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(TEST_REFRESH_TOKEN, response.getRefreshToken());
    }

    @Test
    void shouldThrowExceptionForInvalidUser() {
        // given
        AuthenticationRequestDTO request = testAuthenticationRequest();
        Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        // when + then
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.authenticate(request));
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        //given
        User user = testUserRoleCustomer();
        ChangePasswordRequestDTO request = testChangePasswordRequest();

        Principal connectedUser = mock(Principal.class);
        when(connectedUser.getName()).thenReturn(TEST_USER_EMAIL);

        when(userDAO.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(request.getCurrentPassword(), Objects.requireNonNull(user).password()))
                .thenReturn(true);
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn(request.getNewPassword());
        //when
        userServiceImpl.changePassword(request, connectedUser);

        //then
        verify(userDAO, times(1)).save(user.withPassword(request.getNewPassword()));
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenChangingPassword() {
        //given
        ChangePasswordRequestDTO request = testChangePasswordRequest();

        Principal connectedUser = mock(Principal.class);
        when(connectedUser.getName()).thenReturn(TEST_USER_EMAIL);
        when(userDAO.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.empty());
        //when,then
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.changePassword(request, connectedUser));
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenChangingPassword() {
        //given
        User user = testUserRoleCustomer();
        ChangePasswordRequestDTO request = testChangePasswordRequest();

        Principal connectedUser = mock(Principal.class);
        when(connectedUser.getName()).thenReturn(TEST_USER_EMAIL);
        when(userDAO.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(request.getCurrentPassword(), Objects.requireNonNull(user).password()))
                .thenReturn(false);
        //when,then
        assertThrows(InvalidPasswordException.class, () -> userServiceImpl.changePassword(request, connectedUser));
    }

    @Test
    void shouldThrowPasswordMismatchExceptionWhenChangingPassword() {
        //given
        User user = testUserRoleCustomer();
        ChangePasswordRequestDTO request = testChangePasswordRequest();
        request.setNewPassword("12345");

        Principal connectedUser = mock(Principal.class);
        when(connectedUser.getName()).thenReturn(TEST_USER_EMAIL);
        when(userDAO.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(request.getCurrentPassword(), Objects.requireNonNull(user).password()))
                .thenReturn(true);

        //when,then
        assertThrows(PasswordMismatchException.class, () -> userServiceImpl.changePassword(request, connectedUser));
    }
}