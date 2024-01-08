package pl.cottageconnect.like.controller.dto;

import pl.cottageconnect.like.enums.LikeableType;

public record LikeDTO(Long likeId, LikeableType type, Long likeableId, Integer userId) {
}
