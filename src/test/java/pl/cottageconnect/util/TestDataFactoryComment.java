package pl.cottageconnect.util;

import pl.cottageconnect.comment.controller.dto.CommentRequestDTO;
import pl.cottageconnect.comment.controller.dto.CommentResponseDTO;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.entity.CommentEntity;
import pl.cottageconnect.comment.enums.CommentableType;
import pl.cottageconnect.security.domain.User;

public class TestDataFactoryComment {

    public static Comment testComment() {
        return Comment.builder()
                .content("content")
                .type(CommentableType.VILLAGE)
                .rating(5)
                .build();
    }

    public static Comment testComment2() {
        return Comment.builder()
                .commentId(1L)
                .content("content")
                .rating(5)
                .type(CommentableType.VILLAGE)
                .commentableId(1L)
                .user(User.builder().userId(1).build())
                .build();
    }

    public static CommentEntity testCommentEntity() {
        return CommentEntity.builder()
                .content("content")
                .type(CommentableType.VILLAGE)
                .commentableId(1L)
                .rating(5)
                .build();
    }

    public static CommentEntity testCommentEntity2() {
        return CommentEntity.builder()
                .content("content")
                .type(CommentableType.VILLAGE)
                .commentableId(1L)
                .rating(5)
                .build();
    }

    public static Comment testComment4() {
        return Comment.builder()
                .content("content")
                .type(CommentableType.VILLAGE)
                .rating(5)
                .build();
    }

    public static CommentResponseDTO testCommentResponseDTO() {
        return CommentResponseDTO.builder()
                .content("content")
                .type(CommentableType.VILLAGE)
                .rating(5)
                .build();
    }

    public static CommentRequestDTO testCommentRequestDTO() {
        return CommentRequestDTO.builder()
                .content("content")
                .rating(5)
                .build();
    }

}
