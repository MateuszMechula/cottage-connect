package pl.cottageconnect.like.controller.dto;

import lombok.Builder;
import pl.cottageconnect.like.enums.LikeableType;

@Builder
public record LikeDTO(Long likeId, LikeableType type, Long likeableId, Integer userId) {
}
