package pl.cottageconnect.comment.controller.dto.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.comment.controller.dto.CommentRequestDTO;
import pl.cottageconnect.comment.domain.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.util.TestDataFactoryComment.testComment;
import static pl.cottageconnect.util.TestDataFactoryComment.testCommentRequestDTO;

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
        assertEquals(comment.getContent(), mappedComment.getContent());
        assertEquals(comment.getRating(), mappedComment.getRating());
    }
}