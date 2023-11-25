package pl.cottageconnect.security.service.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.cottageconnect.security.configuration.JwtService;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.exception.EmailAlreadyExistsException;
import pl.cottageconnect.security.exception.InvalidPasswordException;
import pl.cottageconnect.security.exception.PasswordMismatchException;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.security.service.dao.UserDAO;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryUser.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {
    @Mock
    private UserDAO userDAO;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        //given
        User user = testUser();
        RegistrationRequestDTO request = testRegistrationRequest();

        when(userDAO.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(jwtService.generateToken(anyString())).thenReturn(TEST_ACCESS_TOKEN);
        when(jwtService.createRefreshToken(anyString())).thenReturn(TEST_REFRESH_TOKEN);
        //when
        AuthenticationResponseDTO response = userService.register(request);

        //then
        assertNotNull(response);
        assertEquals(TEST_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(TEST_REFRESH_TOKEN, response.getRefreshToken());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        //given
        User user = testUser();
        RegistrationRequestDTO request = testRegistrationRequest();

        when(userDAO.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //when, then
        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(request));
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
        AuthenticationResponseDTO response = userService.authenticate(authenticationRequestDTO);

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
        assertThrows(UsernameNotFoundException.class, () -> userService.authenticate(request));
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        //given
        User user = testUser();
        ChangePasswordRequestDTO request = testChangePasswordRequest();

        Principal connectedUser = mock(Principal.class);
        when(connectedUser.getName()).thenReturn(TEST_USER_EMAIL);
        when(userDAO.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(request.getCurrentPassword(), Objects.requireNonNull(user).getPassword()))
                .thenReturn(true);
        //when
        userService.changePassword(request, connectedUser);

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
        assertThrows(UsernameNotFoundException.class, () -> userService.changePassword(request, connectedUser));
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenChangingPassword() {
        //given
        User user = testUser();
        ChangePasswordRequestDTO request = testChangePasswordRequest();

        Principal connectedUser = mock(Principal.class);
        when(connectedUser.getName()).thenReturn(TEST_USER_EMAIL);
        when(userDAO.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(request.getCurrentPassword(), Objects.requireNonNull(user).getPassword()))
                .thenReturn(false);
        //when,then
        assertThrows(InvalidPasswordException.class, () -> userService.changePassword(request, connectedUser));
    }

    @Test
    void shouldThrowPasswordMismatchExceptionWhenChangingPassword() {
        //given
        User user = testUser();
        ChangePasswordRequestDTO request = testChangePasswordRequest();
        request.setNewPassword("12345");

        Principal connectedUser = mock(Principal.class);
        when(connectedUser.getName()).thenReturn(TEST_USER_EMAIL);
        when(userDAO.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(request.getCurrentPassword(), Objects.requireNonNull(user).getPassword()))
                .thenReturn(true);

        //when,then
        assertThrows(PasswordMismatchException.class, () -> userService.changePassword(request, connectedUser));
    }
}