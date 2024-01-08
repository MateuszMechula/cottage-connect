package pl.cottageconnect.owner.domain;

import lombok.*;
import pl.cottageconnect.village.domain.Village;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "ownerId")
@ToString(of = {"ownerId", "firstname", "lastname", "phone", "userId"})
public class Owner {

    Long ownerId;
    String firstname;
    String lastname;
    String phone;
    Integer userId;
    Set<Village> village;
}
