package pl.cottageconnect.owner.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import pl.cottageconnect.configuration.AbstractJpa;
import pl.cottageconnect.owner.entity.OwnerEntity;
import pl.cottageconnect.security.entity.UserEntity;
import pl.cottageconnect.security.repository.jpa.UserJpaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cottageconnect.util.TestDataFactoryOwner.testOwnerEntity;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource(properties = "test.name=OwnerJpaRepositoryTest")
class OwnerJpaRepositoryTest extends AbstractJpa {

    private final OwnerJpaRepository ownerJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Test
    void shouldFindOwnerByUserIdSuccessfully() {
        //given
        Integer userId = 1;
        UserEntity userEntity = testUserEntity();
        userEntity.setUserId(userId);

        OwnerEntity ownerEntity = testOwnerEntity();
        userJpaRepository.saveAndFlush(userEntity);
        ownerJpaRepository.saveAndFlush(ownerEntity);

        //when
        Optional<OwnerEntity> ownerFounded = ownerJpaRepository.findOwnerByUserId(userId);
        //then
        assertTrue(ownerFounded.isPresent());
        OwnerEntity owner = ownerFounded.get();
        assertEquals(ownerEntity.getOwnerId(), owner.getOwnerId());
        assertEquals(ownerEntity.getFirstname(), owner.getFirstname());
        assertEquals(ownerEntity.getLastname(), owner.getLastname());
        assertEquals(ownerEntity.getPhone(), owner.getPhone());
        assertEquals(ownerEntity.getUserId(), owner.getUserId());
    }
}