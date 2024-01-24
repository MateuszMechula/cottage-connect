package pl.cottageconnect.util;

import pl.cottageconnect.cottage.domain.Cottage;

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
}
