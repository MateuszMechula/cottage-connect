package pl.cottageconnect.security.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record RegistrationRequestDTO(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String role,
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotBlank @Pattern(regexp = "^\\+?[0-9]{6,14}$") String phone) {
}
