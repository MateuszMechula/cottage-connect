package pl.cottageconnect.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        User user = testUserRoleCustomer();

        when(userJpaRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.ofNullable(userEntity));
        if (userEntity != null) {
            when(userEntityMapper.mapFromEntity(userEntity)).thenReturn(user);
        }
        //when
        Optional<User> optionalUser = userRepository.findByEmail(TEST_USER_EMAIL);
        //then
        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().email()).isEqualTo(user.email());
        assertThat(optionalUser.get().password()).isEqualTo(user.password());
    }

    @Test
    void shouldSaveUserSuccessfully() {
        //given
        UserEntity userEntity = testUserEntity();
        User user = testUserRoleCustomer();

        when(userEntityMapper.mapToEntity(user)).thenReturn(userEntity);
        //when
        userRepository.save(user);
        //then
        verify(userEntityMapper, times(1)).mapToEntity(user);
        verify(userJpaRepository, times(1)).save(userEntity);

    }
}