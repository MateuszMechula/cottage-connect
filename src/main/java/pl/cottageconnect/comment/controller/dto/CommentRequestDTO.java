package pl.cottageconnect.comment.controller.dto;


import lombok.Builder;

@Builder
public record CommentRequestDTO(String content, Integer rating) {
}
