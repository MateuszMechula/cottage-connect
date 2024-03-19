package pl.cottageconnect.security;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserRoleCustomer;

class UserEntityMapperTest {
    private final UserEntityMapper mapper = Mappers.getMapper(UserEntityMapper.class);

    @Test
    void shouldMapUserFromUserEntity() {
        //given
        UserEntity userEntity = testUserEntity();
        //when
        User mappedUser = mapper.mapFromEntity(userEntity);
        //then
        assertThat(mappedUser.email()).isEqualTo(userEntity.getEmail());
        assertThat(mappedUser.password()).isEqualTo(userEntity.getPassword());
    }

    @Test
    void shouldMapUserToUserEntity() {
        //given
        User user = testUserRoleCustomer();
        //when
        UserEntity mappedUser = mapper.mapToEntity(user);
        //then
        assertThat(mappedUser.getEmail()).isEqualTo(user.email());
        assertThat(mappedUser.getPassword()).isEqualTo(user.password());
    }
}