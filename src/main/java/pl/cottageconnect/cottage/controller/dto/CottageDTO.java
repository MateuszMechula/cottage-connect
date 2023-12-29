package pl.cottageconnect.cottage.controller.dto;

import java.math.BigDecimal;

public record CottageDTO(Integer cottageNumber, Integer cottageSize, BigDecimal price,
                         String description) {
}
