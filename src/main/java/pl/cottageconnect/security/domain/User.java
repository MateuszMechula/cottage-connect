package pl.cottageconnect.security.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;
import pl.cottageconnect.security.entity.RoleEntity;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "id")
public class User {
    Integer id;
    String email;
    String password;
    Set<RoleEntity> roles;

}
