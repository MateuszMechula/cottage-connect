package pl.cottageconnect.village.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VillagePostResponseDTO {

    Long villagePostId;
    String title;
    String content;
    LocalDateTime createdAt;
}
