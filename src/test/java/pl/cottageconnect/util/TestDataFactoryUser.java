package pl.cottageconnect.util;

import lombok.experimental.UtilityClass;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.security.controller.dto.AuthenticationRequestDTO;
import pl.cottageconnect.security.controller.dto.AuthenticationResponseDTO;
import pl.cottageconnect.security.controller.dto.ChangePasswordRequestDTO;
import pl.cottageconnect.security.controller.dto.RegistrationRequestDTO;
import pl.cottageconnect.security.domain.Role;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.RoleEntity;
import pl.cottageconnect.security.entity.UserEntity;

import java.util.Set;

@UtilityClass
public class TestDataFactoryUser {
    public static final String TEST_ACCESS_TOKEN = "testAccessToken";
    public static final String TEST_REFRESH_TOKEN = "testRefreshToken";
    public static final String TEST_USER_EMAIL = "jankowalski@gmail.com";
    public static final String TEST_USER_PASSWORD = "testPassword";
    public static final String TEST_USER_NEW_PASSWORD = "testPassword2";
    public static final Integer TEST_USER_ID = 1;
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String PHONE = "505403330";
    public static final String ROLE_OWNER = "OWNER";

    public static RegistrationRequestDTO testRegistrationRequestCustomer() {
        return RegistrationRequestDTO.builder()
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .role(ROLE_CUSTOMER)
                .firstname(FIRSTNAME)
                .lastname(LASTNAME)
                .phone(PHONE)
                .build();
    }

    public static RegistrationRequestDTO testRegistrationRequestOwner() {
        return RegistrationRequestDTO.builder()
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .role(ROLE_OWNER)
                .firstname(FIRSTNAME)
                .lastname(LASTNAME)
                .phone(PHONE)
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
                .roles(Set.of(Role.builder().build()))
                .build();
    }

    public static User testUserWithId() {
        return User.builder()
                .userId(TEST_USER_ID)
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .roles(Set.of(Role.builder().build()))
                .build();
    }

    public static UserEntity testUserEntity() {
        return UserEntity.builder()
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .roles(Set.of(RoleEntity
                        .builder()
                        .roleId(1)
                        .build()))
                .build();
    }

    public static ChangePasswordRequestDTO testChangePasswordRequest() {
        return ChangePasswordRequestDTO.builder()
                .currentPassword(TEST_USER_PASSWORD)
                .newPassword(TEST_USER_NEW_PASSWORD)
                .confirmationPassword(TEST_USER_NEW_PASSWORD)
                .build();
    }

    public static Customer testCustomer() {
        return Customer.builder()
                .firstname(FIRSTNAME)
                .lastname(LASTNAME)
                .phone(PHONE)
                .build();
    }

    public static Owner testOwner() {
        return Owner.builder()
                .firstname(FIRSTNAME)
                .lastname(LASTNAME)
                .phone(PHONE)
                .build();
    }
}
