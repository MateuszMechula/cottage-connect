package pl.cottageconnect.village.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record VillagePostResponseDTO(Long villagePostId, String title, String content, LocalDateTime createdAt) {
}
