package pl.cottageconnect.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@AllArgsConstructor
public class UserRepositoryManager {
    private final UserJpaRepository userJpaRepository;

    public void deleteAll() {
        userJpaRepository.deleteAll();
    }

    public void save(UserEntity userEntity) {
        userJpaRepository.save(userEntity);
    }

    public void saveAndFlush(UserEntity userEntity) {
        userJpaRepository.saveAndFlush(userEntity);
    }
}
