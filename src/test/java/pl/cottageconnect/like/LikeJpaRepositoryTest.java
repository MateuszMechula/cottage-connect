package pl.cottageconnect.like;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.cottageconnect.configuration.AbstractJpa;
import pl.cottageconnect.security.UserEntity;
import pl.cottageconnect.security.UserRepositoryManager;
import pl.cottageconnect.util.TestDataFactoryUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cottageconnect.util.TestDataFactoryLike.testLikeEntity;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource(properties = "test.name=LikeJpaRepositoryTest")
@Import(UserRepositoryManager.class)
class LikeJpaRepositoryTest extends AbstractJpa {

    private final LikeJpaRepository likeJpaRepository;
    private final UserRepositoryManager userRepositoryManager;

    @Test
    void shouldFindLikeByLikeableAndUserSuccessfully() {
        //given
        Long likeableId = 1L;
        Integer userId = 1;
        LikeableType type = LikeableType.COMMENT;
        UserEntity userEntity = TestDataFactoryUser.testUserEntity();
        LikeEntity like = testLikeEntity();
        like.setUser(userEntity);

        userRepositoryManager.saveAndFlush(userEntity);
        likeJpaRepository.saveAndFlush(like);

        //when
        Optional<LikeEntity> likeByLikeableAndUser = likeJpaRepository.findLikeByLikeableAndUser(likeableId, type, userId);

        //then
        assertTrue(likeByLikeableAndUser.isPresent());
        LikeEntity likeEntity = likeByLikeableAndUser.get();
        assertEquals(likeableId, likeEntity.getLikeableId());
        assertEquals(type, likeEntity.getType());
        assertEquals(userId, likeEntity.getUser().getUserId());
    }
}