package pl.cottageconnect.comment.domain;

import lombok.*;
import pl.cottageconnect.comment.enums.CommentableType;
import pl.cottageconnect.security.entity.UserEntity;

@With
@Value
@Builder
@EqualsAndHashCode(of = "commentId")
@ToString(of = {"commentId", "content", "type", "commentableId", "rating"})
public class Comment {

    Long commentId;
    String content;
    CommentableType type;
    Long commentableId;
    Integer rating;
    UserEntity user;
}
