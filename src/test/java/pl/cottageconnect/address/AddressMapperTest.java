package pl.cottageconnect.address;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static pl.cottageconnect.address.TestDataFactoryAddress.testAddressDTO;

class AddressMapperTest {

    private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);

    @Test
    void shouldMapAddressDTOtoAddress() {
        //given
        AddressDTO addressDTO = testAddressDTO();
        //when
        Address addressMapped = addressMapper.map(addressDTO);
        //then
        Assertions.assertEquals(addressDTO.street(), addressMapped.street());
        Assertions.assertEquals(addressDTO.postalCode(), addressMapped.postalCode());
        Assertions.assertEquals(addressDTO.city(), addressMapped.city());
        Assertions.assertEquals(addressDTO.voivodeship(), addressMapped.voivodeship());
        Assertions.assertEquals(addressDTO.country(), addressMapped.country());
    }
}