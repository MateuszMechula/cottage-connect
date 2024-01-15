package pl.cottageconnect.cottage.controller.dto;

import java.math.BigDecimal;

public record CottageDTO(Long cottageId, Integer cottageNumber, Integer cottageSize, BigDecimal price,
                         String description) {
}
