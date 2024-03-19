package pl.cottageconnect.comment.controller;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.comment.Comment;
import pl.cottageconnect.comment.controller.dto.CommentRequestDTO;
import pl.cottageconnect.comment.controller.mapper.CommentRequestMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.comment.TestDataFactoryComment.testComment;
import static pl.cottageconnect.comment.TestDataFactoryComment.testCommentRequestDTO;

class CommentRequestMapperTest {

    private final CommentRequestMapper commentRequestMapper = Mappers.getMapper(CommentRequestMapper.class);

    @Test
    void shouldMapCommentRequestDTOtoComment() {
        //given
        CommentRequestDTO commentRequestDTO = testCommentRequestDTO();
        Comment comment = testComment();
        //when
        Comment mappedComment = commentRequestMapper.map(commentRequestDTO);
        //then
        assertEquals(comment.content(), mappedComment.content());
        assertEquals(comment.rating(), mappedComment.rating());
    }
}