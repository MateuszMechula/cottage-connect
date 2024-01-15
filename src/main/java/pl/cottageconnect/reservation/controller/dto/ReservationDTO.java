package pl.cottageconnect.reservation.controller.dto;

import pl.cottageconnect.cottage.controller.dto.CottageDTO;

import java.time.OffsetDateTime;

public record ReservationDTO(Long reservationId, OffsetDateTime dayIn, OffsetDateTime dayOut, Boolean status,
                             CottageDTO cottage) {
}
