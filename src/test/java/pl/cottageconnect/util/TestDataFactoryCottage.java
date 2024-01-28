package pl.cottageconnect.util;

import pl.cottageconnect.cottage.controller.dto.CottageDTO;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.entity.CottageEntity;
import pl.cottageconnect.village.domain.Village;

import java.math.BigDecimal;

public class TestDataFactoryCottage {

    public static Cottage testCottage() {
        return Cottage.builder()
                .cottageId(1L)
                .cottageNumber(2)
                .cottageSize(2)
                .price(BigDecimal.valueOf(50))
                .description("exclusive")
                .build();
    }

    public static Cottage testCottage2() {
        return Cottage.builder()
                .cottageId(2L)
                .cottageNumber(2)
                .cottageSize(2)
                .price(BigDecimal.valueOf(50))
                .description("exclusive")
                .village(Village.builder()
                        .villageId(1L)
                        .build())
                .build();
    }

    public static Cottage testCottage3() {
        return Cottage.builder()
                .cottageId(3L)
                .cottageNumber(2)
                .cottageSize(2)
                .price(BigDecimal.valueOf(50))
                .description("exclusive")
                .village(Village.builder()
                        .villageId(1L)
                        .build())
                .build();
    }

    public static CottageDTO testCottageDTO() {
        return CottageDTO.builder()
                .cottageId(1L)
                .cottageNumber(2)
                .cottageSize(2)
                .price(BigDecimal.valueOf(50))
                .description("exclusive")
                .build();
    }

    public static CottageEntity testCottageEntity() {
        return CottageEntity.builder()
                .cottageId(1L)
                .cottageNumber(2)
                .cottageSize(2)
                .price(BigDecimal.valueOf(50))
                .description("exclusive")
                .build();

    }
}
