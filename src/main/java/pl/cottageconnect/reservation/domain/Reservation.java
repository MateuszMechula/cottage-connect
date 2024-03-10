package pl.cottageconnect.reservation.domain;

import lombok.Builder;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.customer.domain.Customer;

import java.time.OffsetDateTime;

@Builder
public record Reservation(Long reservationId, OffsetDateTime dayIn, OffsetDateTime dayOut, Boolean status,
                          Customer customer, Cottage cottage) {
}
