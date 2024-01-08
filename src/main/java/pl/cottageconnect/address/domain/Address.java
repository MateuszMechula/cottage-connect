package pl.cottageconnect.address.domain;

import lombok.*;
import pl.cottageconnect.village.domain.Village;

@With
@Value
@Builder
@EqualsAndHashCode(of = "addressId")
@ToString(of = {"addressId", "street", "postalCode", "city", "voivodeship", "country"})
public class Address {

    Long addressId;
    String street;
    String postalCode;
    String city;
    String voivodeship;
    String country;
    Village village;
}
