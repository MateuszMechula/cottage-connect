package pl.cottageconnect.village.controller.dto;

import lombok.Builder;

@Builder
public record VillagePostRequestDTO(String title, String content) {
}
