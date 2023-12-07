package pl.cottageconnect.customer.domain;

import lombok.*;
import pl.cottageconnect.reservation.entity.ReservationEntity;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "customerId")
@ToString(of = {"customerId", "firstname", "lastname", "phone", "userId"})
public class Customer {

    Long customerId;
    String firstname;
    String lastname;
    String phone;
    Integer userId;
    Set<ReservationEntity> reservations;
}
