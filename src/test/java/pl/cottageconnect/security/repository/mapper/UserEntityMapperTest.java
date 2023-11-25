package pl.cottageconnect.security.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.cottageconnect.util.TestDataFactoryUser.testUser;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;

@ExtendWith(MockitoExtension.class)
class UserEntityMapperTest {
    @InjectMocks
    private UserEntityMapper userEntityMapper;

    @Test
    void shouldMapUserFromUserEntity() {
        //given
        UserEntity userEntity = testUserEntity();
        //when
        User mappedUser = userEntityMapper.mapFromEntity(userEntity);
        //then
        assertThat(mappedUser.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(mappedUser.getPassword()).isEqualTo(userEntity.getPassword());
    }

    @Test
    void shouldMapUserToUserEntity() {
        //given
        User user = testUser();
        //when
        UserEntity mappedUser = userEntityMapper.mapToEntity(user);
        //then
        assertThat(mappedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(mappedUser.getPassword()).isEqualTo(user.getPassword());
    }
}