package pl.cottageconnect.security.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "roleId")
@ToString(of = {"roleId", "role"})
public class Role {

    Integer roleId;
    String role;
}
