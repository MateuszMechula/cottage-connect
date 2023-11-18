package pl.cottageconnect.security.controller.dto.mapper;

import org.springframework.stereotype.Component;
import pl.cottageconnect.security.controller.dto.RegistrationRequest;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.RoleEntity;
import pl.cottageconnect.security.enums.RoleEnum;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserMapperDTO {
    public User mapToUser(RegistrationRequest registrationRequest, RoleEnum role) {
        return User.builder()
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword())
                .roles(extractRoles(role))
                .build();
    }

    private Set<RoleEntity> extractRoles(RoleEnum role) {
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(RoleEntity.builder().role(role.name()).build());
        return roles;
    }
}
