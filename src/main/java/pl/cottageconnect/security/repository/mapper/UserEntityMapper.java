package pl.cottageconnect.security.repository.mapper;

import org.springframework.stereotype.Component;
import pl.cottageconnect.security.domain.Role;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.RoleEntity;
import pl.cottageconnect.security.entity.UserEntity;

import java.util.stream.Collectors;

@Component
public class UserEntityMapper {
    public User mapFromEntity(UserEntity entity) {
        return User.builder()
                .userId(entity.getUserId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .roles(entity.getRoles().stream().map(role -> Role
                        .builder()
                        .roleId(role.getRoleId())
                        .role(role.getRole())
                        .build()).collect(Collectors.toSet()))
                .build();
    }

    public UserEntity mapToEntity(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(role -> RoleEntity
                                .builder()
                                .roleId(role.getRoleId())
                                .role(role.getRole())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
