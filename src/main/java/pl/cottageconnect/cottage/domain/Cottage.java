package pl.cottageconnect.cottage.domain;

import lombok.*;
import pl.cottageconnect.reservation.entity.ReservationEntity;
import pl.cottageconnect.village.entity.VillageEntity;

import java.math.BigDecimal;
import java.util.Set;


@With
@Value
@Builder
@EqualsAndHashCode(of = "cottageId")
@ToString(of = {"cottageId", "cottageNumber", "cottageSize", "price", "description"})
public class Cottage {

    Long cottageId;
    Integer cottageNumber;
    Integer cottageSize;
    BigDecimal price;
    String description;
    VillageEntity village;
    Set<ReservationEntity> reservations;

}
