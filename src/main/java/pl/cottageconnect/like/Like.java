package pl.cottageconnect.like;

import lombok.Builder;
import lombok.With;
import pl.cottageconnect.security.User;

@With
@Builder
public record Like(Long likeId, LikeableType type, Long likeableId, User user) {
}
