package pl.cottageconnect.comment;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import pl.cottageconnect.configuration.AbstractJpa;
import pl.cottageconnect.security.UserEntity;
import pl.cottageconnect.security.UserRepositoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.comment.TestDataFactoryComment.testCommentEntity;
import static pl.cottageconnect.comment.TestDataFactoryComment.testCommentEntity2;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource(properties = "test.name=CommentJpaRepositoryTest")
@Import(UserRepositoryManager.class)
class CommentJpaRepositoryTest extends AbstractJpa {

    private final UserRepositoryManager userRepositoryManager;
    private final CommentJpaRepository commentJpaRepository;


    @Test
    void shouldGetCommentsByCommentableId() {
        //given
        Long commentableId = 1L;
        Pageable pageable = PageRequest.of(0, 2, Sort.by("commentId").ascending());
        UserEntity userEntity = testUserEntity();
        userRepositoryManager.save(userEntity);
        CommentEntity firstComment = testCommentEntity();
        firstComment.setUser(userEntity);
        CommentEntity secondComment = testCommentEntity2();
        secondComment.setUser(userEntity);
        commentJpaRepository.save(firstComment);
        commentJpaRepository.save(secondComment);
        //when
        Page<CommentEntity> commentsByCommentableId = commentJpaRepository.getCommentsByCommentableId(commentableId, pageable);
        //then
        assertEquals(2, commentsByCommentableId.getTotalElements());
        assertEquals(firstComment, commentsByCommentableId.getContent().get(0));
        assertEquals(secondComment, commentsByCommentableId.getContent().get(1));
    }
}