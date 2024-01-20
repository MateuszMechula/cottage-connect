package pl.cottageconnect.comment.controller.dto.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.comment.controller.dto.CommentResponseDTO;
import pl.cottageconnect.comment.domain.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.util.TestDataFactoryComment.testComment2;

class CommentResponseMapperTest {
    private final CommentResponseMapper commentResponseMapper = Mappers.getMapper(CommentResponseMapper.class);

    @Test
    void shouldMapCommentToDTO() {
        //given
        Comment comment = testComment2();
        //when
        CommentResponseDTO dto = commentResponseMapper.mapToDTO(comment);
        //then
        assertEquals(comment.getCommentId(), dto.commentId());
        assertEquals(comment.getContent(), dto.content());
        assertEquals(comment.getRating(), dto.rating());
        assertEquals(comment.getType(), dto.type());
        assertEquals(comment.getCommentableId(), dto.commentableId());
        assertEquals(comment.getUser().getUserId(), dto.userId());
    }
}