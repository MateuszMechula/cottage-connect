package pl.cottageconnect.village.domain;

import lombok.*;
import pl.cottageconnect.security.entity.UserEntity;
import pl.cottageconnect.village.entity.VillageEntity;

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
    UserEntity user;
    VillageEntity village;

}
