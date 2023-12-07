package pl.cottageconnect.security.domain;

import lombok.*;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "userId")
@ToString(of = {"userId", "email", "password"})
public class User {
    Integer userId;
    String email;
    String password;
    Set<Role> roles;

}
