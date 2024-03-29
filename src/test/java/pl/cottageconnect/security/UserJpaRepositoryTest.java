package pl.cottageconnect.security;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import pl.cottageconnect.configuration.AbstractJpa;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.cottageconnect.util.TestDataFactoryUser.TEST_USER_EMAIL;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource(properties = "test.name=UserJpaRepositoryTest")
class UserJpaRepositoryTest extends AbstractJpa {

    private final UserJpaRepository repository;

    @Test
    void shouldFindUserByEmailSuccessfulLy() {
        //given
        repository.saveAndFlush(testUserEntity());
        //when
        Optional<UserEntity> userEntity = repository.findByEmail(TEST_USER_EMAIL);
        //then
        assertThat(userEntity).isPresent();
    }
}