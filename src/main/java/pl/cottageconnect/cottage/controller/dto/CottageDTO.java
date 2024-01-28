package pl.cottageconnect.cottage.controller.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CottageDTO(Long cottageId, Integer cottageNumber, Integer cottageSize, BigDecimal price,
                         String description) {
}
