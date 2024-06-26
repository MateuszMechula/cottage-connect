package pl.cottageconnect.customer;

import lombok.Builder;
import lombok.With;
import pl.cottageconnect.reservation.ReservationEntity;
import pl.cottageconnect.security.Person;

import java.util.Set;

@With
@Builder
public record Customer(Long customerId, String firstname, String lastname, String phone, Integer userId,
                       Set<ReservationEntity> reservations) implements Person {
}
