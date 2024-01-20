package pl.cottageconnect.address.controller.dto.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.address.controller.dto.AddressDTO;
import pl.cottageconnect.address.domain.Address;

import static pl.cottageconnect.util.TestDataFactoryAddress.testAddressDTO;

class AddressMapperTest {

    private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);

    @Test
    void shouldMapAddressDTOtoAddress() {
        //given
        AddressDTO addressDTO = testAddressDTO();
        //when
        Address addressMapped = addressMapper.map(addressDTO);
        //then
        Assertions.assertEquals(addressDTO.getStreet(), addressMapped.getStreet());
        Assertions.assertEquals(addressDTO.getPostalCode(), addressMapped.getPostalCode());
        Assertions.assertEquals(addressDTO.getCity(), addressMapped.getCity());
        Assertions.assertEquals(addressDTO.getVoivodeship(), addressMapped.getVoivodeship());
        Assertions.assertEquals(addressDTO.getCountry(), addressMapped.getCountry());
    }
}