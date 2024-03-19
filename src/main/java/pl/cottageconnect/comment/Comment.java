package pl.cottageconnect.comment;

import lombok.Builder;
import pl.cottageconnect.security.User;

@Builder
public record Comment(Long commentId, String content, CommentableType type, Long commentableId, Integer rating,
                      User user) {
}
