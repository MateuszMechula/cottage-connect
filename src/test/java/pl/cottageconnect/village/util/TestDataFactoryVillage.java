package pl.cottageconnect.village.util;

import pl.cottageconnect.address.Address;
import pl.cottageconnect.address.AddressDTO;
import pl.cottageconnect.village.Village;
import pl.cottageconnect.village.controller.dto.VillageDTO;

public class TestDataFactoryVillage {

    public static VillageDTO testVillageDTO() {
        return VillageDTO.builder()
                .name("name")
                .description("description")
                .addressDTO(AddressDTO.builder()
                        .street("street")
                        .postalCode("11-400")
                        .city("city")
                        .voivodeship("voivodeship")
                        .country("country")
                        .build())
                .build();
    }

    public static Village testVillage() {
        return Village.builder()
                .name("name")
                .description("description")
                .address(Address.builder()
                        .street("street")
                        .postalCode("11-400")
                        .city("city")
                        .voivodeship("voivodeship")
                        .country("country")
                        .build())
                .build();
    }
}
