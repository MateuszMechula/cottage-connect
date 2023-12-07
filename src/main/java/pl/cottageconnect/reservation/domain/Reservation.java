package pl.cottageconnect.reservation.domain;

import lombok.*;
import pl.cottageconnect.cottage.entity.CottageEntity;
import pl.cottageconnect.customer.entity.CustomerEntity;

import java.time.LocalDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "reservationId")
@ToString(of = {"reservationId", "dayIn", "dayOut", "status"})
public class Reservation {

    Long reservationId;
    LocalDateTime dayIn;
    LocalDateTime dayOut;
    Boolean status;
    CustomerEntity customer;
    CottageEntity cottage;
}
