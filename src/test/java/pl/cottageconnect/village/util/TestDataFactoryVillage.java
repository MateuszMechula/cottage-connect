package pl.cottageconnect.village.util;

import pl.cottageconnect.address.controller.dto.AddressDTO;
import pl.cottageconnect.address.domain.Address;
import pl.cottageconnect.village.controller.dto.VillageDTO;
import pl.cottageconnect.village.domain.Village;

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
