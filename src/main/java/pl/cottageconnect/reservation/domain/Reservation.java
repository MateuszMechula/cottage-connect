package pl.cottageconnect.reservation.domain;

import lombok.*;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.customer.domain.Customer;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "reservationId")
@ToString(of = {"reservationId", "dayIn", "dayOut", "status"})
public class Reservation {

    Long reservationId;
    OffsetDateTime dayIn;
    OffsetDateTime dayOut;
    Boolean status;
    Customer customer;
    Cottage cottage;
}
