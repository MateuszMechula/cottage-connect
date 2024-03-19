package pl.cottageconnect.comment.controller.dto;


import lombok.Builder;
import pl.cottageconnect.comment.CommentableType;


@Builder
public record CommentResponseDTO(Long commentId, String content, Integer rating, CommentableType type,
                                 Long commentableId, Integer userId) {
}
