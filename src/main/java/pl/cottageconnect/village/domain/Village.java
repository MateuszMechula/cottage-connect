package pl.cottageconnect.village.domain;

import lombok.*;
import pl.cottageconnect.address.domain.Address;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.owner.domain.Owner;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "villageId")
@ToString(of = {"villageId", "name", "description"})
public class Village {

    Long villageId;
    String name;
    String description;
    Owner owner;
    Address address;
    Set<Cottage> cottages;
    Set<VillagePost> posts;
}
