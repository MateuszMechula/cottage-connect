package pl.cottageconnect.security.repository.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.cottageconnect.util.TestDataFactoryUser.testUser;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;

class UserEntityMapperTest {
    private final UserEntityMapper mapper = Mappers.getMapper(UserEntityMapper.class);

    @Test
    void shouldMapUserFromUserEntity() {
        //given
        UserEntity userEntity = testUserEntity();
        //when
        User mappedUser = mapper.mapFromEntity(userEntity);
        //then
        assertThat(mappedUser.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(mappedUser.getPassword()).isEqualTo(userEntity.getPassword());
    }

    @Test
    void shouldMapUserToUserEntity() {
        //given
        User user = testUser();
        //when
        UserEntity mappedUser = mapper.mapToEntity(user);
        //then
        assertThat(mappedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(mappedUser.getPassword()).isEqualTo(user.getPassword());
    }
}