package pl.cottageconnect.village;

import lombok.Builder;
import lombok.With;
import pl.cottageconnect.security.User;

import java.time.LocalDateTime;

@With
@Builder
public record VillagePost(Long villagePostId,
                          String title,
                          String content,
                          LocalDateTime createdAt,
                          User user,
                          Village village) {
}
