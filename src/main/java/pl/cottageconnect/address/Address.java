package pl.cottageconnect.address;

import lombok.Builder;
import pl.cottageconnect.village.Village;

@Builder
public record Address(Long addressId, String street, String postalCode, String city, String voivodeship, String country,
                      Village village) {
}
