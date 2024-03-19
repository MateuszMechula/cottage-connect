package pl.cottageconnect.comment.controller;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.comment.Comment;
import pl.cottageconnect.comment.CommentEntity;
import pl.cottageconnect.comment.CommentEntityMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.comment.TestDataFactoryComment.testComment;
import static pl.cottageconnect.comment.TestDataFactoryComment.testCommentEntity;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserRoleCustomer;

class CommentEntityMapperTest {

    private final CommentEntityMapper commentEntityMapper = Mappers.getMapper(CommentEntityMapper.class);

    @Test
    void shouldMapCommentToCommentEntity() {
        //given
        Comment comment = Comment.builder().content(testComment().content()).type(testComment().type()).rating(testComment().rating()).user(testUserRoleCustomer()).build();
        //when
        CommentEntity commentEntity = commentEntityMapper.mapToEntity(comment);
        //then
        assertAll(() -> assertEquals(comment.commentId(),
                        commentEntity.getCommentId()),
                () -> assertEquals(comment.content(),
                        commentEntity.getContent()),
                () -> assertEquals(comment.type(),
                        commentEntity.getType()),
                () -> assertEquals(comment.commentableId(),
                        commentEntity.getCommentableId()), () -> assertEquals(comment.rating(),
                        commentEntity.getRating()), () -> assertEquals(comment.user().email(),
                        commentEntity.getUser().getEmail()), () -> assertEquals(comment.user().password(),
                        commentEntity.getUser().getPassword()));
    }

    @Test
    void shouldMapCommentEntityToComment() {
        //given
        CommentEntity commentEntity = testCommentEntity();
        commentEntity.setUser(testUserEntity());
        //when
        Comment comment = commentEntityMapper.mapFromEntity(commentEntity);
        //then
        assertAll(() -> assertEquals(commentEntity.getCommentId(), comment.commentId()), () -> assertEquals(commentEntity.getContent(), comment.content()), () -> assertEquals(commentEntity.getType(), comment.type()), () -> assertEquals(commentEntity.getCommentableId(), comment.commentableId()), () -> assertEquals(commentEntity.getRating(), comment.rating()), () -> assertEquals(commentEntity.getUser().getEmail(), comment.user().email()), () -> assertEquals(commentEntity.getUser().getPassword(), comment.user().password()));
    }
}