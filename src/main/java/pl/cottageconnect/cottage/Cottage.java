package pl.cottageconnect.cottage;

import lombok.Builder;
import lombok.With;
import pl.cottageconnect.reservation.Reservation;
import pl.cottageconnect.village.Village;

import java.math.BigDecimal;
import java.util.Set;

@With
@Builder
public record Cottage(Long cottageId, Integer cottageNumber, Integer cottageSize, BigDecimal price, String description,
                      Village village, Set<Reservation> reservations) {

}
