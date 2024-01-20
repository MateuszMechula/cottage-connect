package pl.cottageconnect.util;

import pl.cottageconnect.address.controller.dto.AddressDTO;

public class TestDataFactoryAddress {

    public static AddressDTO testAddressDTO() {
        return AddressDTO.builder()
                .street("street")
                .postalCode("postalCode")
                .city("city")
                .voivodeship("voivodeship")
                .country("country")
                .build();
    }
}
