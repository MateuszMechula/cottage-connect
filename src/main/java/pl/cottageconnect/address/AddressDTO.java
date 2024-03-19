package pl.cottageconnect.address;

import lombok.Builder;

@Builder
public record AddressDTO(String street, String postalCode, String city, String voivodeship, String country) {
}
