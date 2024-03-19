package pl.cottageconnect.reservation;

import lombok.Builder;
import pl.cottageconnect.cottage.Cottage;
import pl.cottageconnect.customer.Customer;

import java.time.OffsetDateTime;

@Builder
public record Reservation(Long reservationId, OffsetDateTime dayIn, OffsetDateTime dayOut, Boolean status,
                          Customer customer, Cottage cottage) {
}
