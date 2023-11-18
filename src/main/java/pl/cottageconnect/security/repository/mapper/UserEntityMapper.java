package pl.cottageconnect.security.repository.mapper;

import org.springframework.stereotype.Component;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.UserEntity;

@Component
public class UserEntityMapper {
    public User mapFromEntity(UserEntity entity) {
        return User.builder()
                .id(entity.getUserId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .roles(entity.getRoles())
                .build();
    }

    public UserEntity mapToEntity(User user) {
        return UserEntity.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }
}
