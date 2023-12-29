package pl.cottageconnect.cottage.domain;

import lombok.*;
import pl.cottageconnect.reservation.domain.Reservation;
import pl.cottageconnect.village.domain.Village;

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
    Village village;
    Set<Reservation> reservations;

}
