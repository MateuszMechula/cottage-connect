package pl.cottageconnect.comment.controller.dto;


import pl.cottageconnect.comment.enums.CommentableType;

public record CommentResponseDTO(Long commentId, String content, Integer rating, CommentableType type,
                                 Long commentableId, Integer userId) {
}
