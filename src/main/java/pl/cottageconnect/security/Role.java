package pl.cottageconnect.security;

import lombok.Builder;

@Builder
public record Role(Integer roleId,
                   String role) {
}
