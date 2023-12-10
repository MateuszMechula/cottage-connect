package pl.cottageconnect.village.domain;

import lombok.*;
import pl.cottageconnect.security.domain.User;

import java.time.LocalDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "villagePostId")
@ToString(of = {"villagePostId", "title", "content", "createdAt"})
public class VillagePost {

    Long villagePostId;
    String title;
    String content;
    LocalDateTime createdAt;
    User user;
    Village village;
}
