package pl.cottageconnect.comment.repository.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.entity.CommentEntity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.util.TestDataFactoryComment.testComment;
import static pl.cottageconnect.util.TestDataFactoryComment.testCommentEntity;
import static pl.cottageconnect.util.TestDataFactoryUser.testUser;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;

class CommentEntityMapperTest {

    private final CommentEntityMapper commentEntityMapper = Mappers.getMapper(CommentEntityMapper.class);

    @Test
    void shouldMapCommentToCommentEntity() {
        //given
        Comment comment = testComment().withUser(testUser());
        //when
        CommentEntity commentEntity = commentEntityMapper.mapToEntity(comment);
        //then
        assertAll(
                () -> assertEquals(comment.getCommentId(), commentEntity.getCommentId()),
                () -> assertEquals(comment.getContent(), commentEntity.getContent()),
                () -> assertEquals(comment.getType(), commentEntity.getType()),
                () -> assertEquals(comment.getCommentableId(), commentEntity.getCommentableId()),
                () -> assertEquals(comment.getRating(), commentEntity.getRating()),
                () -> assertEquals(comment.getUser().getEmail(), commentEntity.getUser().getEmail()),
                () -> assertEquals(comment.getUser().getPassword(), commentEntity.getUser().getPassword())
        );
    }

    @Test
    void shouldMapCommentEntityToComment() {
        //given
        CommentEntity commentEntity = testCommentEntity();
        commentEntity.setUser(testUserEntity());
        //when
        Comment comment = commentEntityMapper.mapFromEntity(commentEntity);
        //then
        assertAll(
                () -> assertEquals(commentEntity.getCommentId(), comment.getCommentId()),
                () -> assertEquals(commentEntity.getContent(), comment.getContent()),
                () -> assertEquals(commentEntity.getType(), comment.getType()),
                () -> assertEquals(commentEntity.getCommentableId(), comment.getCommentableId()),
                () -> assertEquals(commentEntity.getRating(), comment.getRating()),
                () -> assertEquals(commentEntity.getUser().getEmail(), comment.getUser().getEmail()),
                () -> assertEquals(commentEntity.getUser().getPassword(), comment.getUser().getPassword())
        );
    }
}