package pl.cottageconnect.reservation.controller.dto;

import lombok.Builder;
import pl.cottageconnect.cottage.controller.dto.CottageDTO;

import java.time.OffsetDateTime;

@Builder
public record ReservationDTO(Long reservationId, OffsetDateTime dayIn, OffsetDateTime dayOut, Boolean status,
                             CottageDTO cottage) {
}
