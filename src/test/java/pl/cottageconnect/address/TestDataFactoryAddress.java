package pl.cottageconnect.address;

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
