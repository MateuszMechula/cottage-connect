package pl.cottageconnect.security.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.UserEntity;
import pl.cottageconnect.security.repository.jpa.UserJpaRepository;
import pl.cottageconnect.security.repository.mapper.UserEntityMapper;
import pl.cottageconnect.security.service.dao.UserDAO;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository implements UserDAO {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Optional<User> getUserByUserId(Integer userId) {
        return userJpaRepository.findById(userId)
                .map(userEntityMapper::mapFromEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userEntityMapper::mapFromEntity);
    }

    @Override
    public User save(User user) {
        UserEntity toSave = userEntityMapper.mapToEntity(user);
        UserEntity saved = userJpaRepository.save(toSave);
        return userEntityMapper.mapFromEntity(saved);
    }
}
