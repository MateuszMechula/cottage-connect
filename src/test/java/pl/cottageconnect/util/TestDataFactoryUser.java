package pl.cottageconnect.util;

import lombok.experimental.UtilityClass;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.UserEntity;

@UtilityClass
public class TestDataFactoryUser {
    public static final String TEST_ACCESS_TOKEN = "testAccessToken";
    public static final String TEST_REFRESH_TOKEN = "testRefreshToken";
    public static final String TEST_USER_EMAIL = "jankowalski@gmail.com";
    public static final String TEST_USER_PASSWORD = "testPassword";
    public static final String TEST_USER_NEW_PASSWORD = "testPassword2";
    public static final String ROLE_CUSTOMER = "CUSTOMER";

    public static RegistrationRequestDTO testRegistrationRequest() {
        return RegistrationRequestDTO.builder()
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .role(ROLE_CUSTOMER)
                .build();
    }

    public static AuthenticationRequestDTO testAuthenticationRequest() {
        return AuthenticationRequestDTO.builder()
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .build();
    }

    public static AuthenticationResponseDTO testAuthenticationResponse() {
        return AuthenticationResponseDTO.builder()
                .refreshToken(TEST_REFRESH_TOKEN)
                .accessToken(TEST_ACCESS_TOKEN)
                .build();
    }

    public static User testUser() {
        return User.builder()
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .build();
    }

    public static UserEntity testUserEntity() {
        return UserEntity.builder()
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .build();
    }

    public static ChangePasswordRequestDTO testChangePasswordRequest() {
        return ChangePasswordRequestDTO.builder()
                .currentPassword(TEST_USER_PASSWORD)
                .newPassword(TEST_USER_NEW_PASSWORD)
                .confirmationPassword(TEST_USER_NEW_PASSWORD)
                .build();
    }
}
