package pl.cottageconnect.security;

import lombok.Builder;
import lombok.With;

import java.util.Set;

@With
@Builder
public record User(Integer userId,
                   String email,
                   String password,
                   Set<Role> roles) {
}
