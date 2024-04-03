package pl.cottageconnect.security.controller.dto;

import lombok.Builder;

@Builder
public record AccountDetailsDTO(String email, String role, String firstname, String lastname, String phone) {

}
