package pl.cottageconnect.security.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDTO {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
//    @Pattern(regexp = "^\\+(?:[0-9]●?){6,14}[0-9]$")
    private String phone;
}
