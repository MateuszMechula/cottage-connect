package pl.cottageconnect.comment.controller;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.comment.Comment;
import pl.cottageconnect.comment.controller.dto.CommentResponseDTO;
import pl.cottageconnect.comment.controller.mapper.CommentResponseMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.comment.TestDataFactoryComment.testComment2;

class CommentResponseMapperTest {
    private final CommentResponseMapper commentResponseMapper = Mappers.getMapper(CommentResponseMapper.class);

    @Test
    void shouldMapCommentToDTO() {
        //given
        Comment comment = testComment2();
        //when
        CommentResponseDTO dto = commentResponseMapper.mapToDTO(comment);
        //then
        assertEquals(comment.commentId(), dto.commentId());
        assertEquals(comment.content(), dto.content());
        assertEquals(comment.rating(), dto.rating());
        assertEquals(comment.type(), dto.type());
        assertEquals(comment.commentableId(), dto.commentableId());
        assertEquals(comment.user().userId(), dto.userId());
    }
}