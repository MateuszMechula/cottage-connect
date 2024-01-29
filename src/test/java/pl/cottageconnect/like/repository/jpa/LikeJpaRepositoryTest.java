package pl.cottageconnect.like.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.cottageconnect.configuration.AbstractJpa;
import pl.cottageconnect.like.entity.LikeEntity;
import pl.cottageconnect.like.enums.LikeableType;
import pl.cottageconnect.security.entity.UserEntity;
import pl.cottageconnect.security.repository.jpa.UserJpaRepository;
import pl.cottageconnect.util.TestDataFactoryUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cottageconnect.util.TestDataFactoryLike.testLikeEntity;

@AllArgsConstructor(onConstructor = @__(@Autowired))
class LikeJpaRepositoryTest extends AbstractJpa {

    private final LikeJpaRepository likeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Test
    void shouldFindLikeByLikeableAndUserSuccessfully() {
        //given
        Long likeableId = 1L;
        Integer userId = 1;
        LikeableType type = LikeableType.COMMENT;
        UserEntity userEntity = TestDataFactoryUser.testUserEntity();
        LikeEntity like = testLikeEntity();
        like.setUser(userEntity);

        userJpaRepository.saveAndFlush(userEntity);
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