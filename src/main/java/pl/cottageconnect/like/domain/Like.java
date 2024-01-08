package pl.cottageconnect.like.domain;

import lombok.*;
import pl.cottageconnect.like.enums.LikeableType;
import pl.cottageconnect.security.domain.User;

@With
@Value
@Builder
@EqualsAndHashCode(of = "likeId")
@ToString(of = {"likeId", "type", "likeableId"})
public class Like {

    Long likeId;
    LikeableType type;
    Long likeableId;
    User user;

}
