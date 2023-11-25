package pl.cottageconnect.security.repository.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.UserEntity;
import pl.cottageconnect.security.repository.UserRepository;
import pl.cottageconnect.security.repository.jpa.UserJpaRepository;
import pl.cottageconnect.security.repository.mapper.UserEntityMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryUser.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryUnitTest {
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private UserEntityMapper userEntityMapper;
    @InjectMocks
    private UserRepository userRepository;


    @Test
    void shouldFindUserByEmailSuccessfully() {
        //given
        UserEntity userEntity = testUserEntity();
        User user = testUser();

        when(userJpaRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.ofNullable(userEntity));
        if (userEntity != null) {
            when(userEntityMapper.mapFromEntity(userEntity)).thenReturn(user);
        }
        //when
        Optional<User> optionalUser = userRepository.findByEmail(TEST_USER_EMAIL);
        //then
        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(optionalUser.get().getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void shouldSaveUserSuccessfully() {
        //given
        UserEntity userEntity = testUserEntity();
        User user = testUser();

        when(userEntityMapper.mapToEntity(user)).thenReturn(userEntity);
        //when
        userRepository.save(user);
        //then
        verify(userEntityMapper, times(1)).mapToEntity(user);
        verify(userJpaRepository, times(1)).save(userEntity);

    }
}