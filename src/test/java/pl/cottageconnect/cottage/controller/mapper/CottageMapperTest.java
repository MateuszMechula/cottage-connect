package pl.cottageconnect.cottage.controller.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.cottage.controller.dto.CottageDTO;
import pl.cottageconnect.cottage.domain.Cottage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.util.TestDataFactoryCottage.testCottage;
import static pl.cottageconnect.util.TestDataFactoryCottage.testCottageDTO;

class CottageMapperTest {

    private final CottageMapper cottageMapper = Mappers.getMapper(CottageMapper.class);

    @Test
    void shouldMapCottageDtoToCottage() {
        //given
        Cottage expectedCottage = testCottage();
        CottageDTO cottageDTO = testCottageDTO();
        //when
        Cottage cottage = cottageMapper.map(cottageDTO);
        //then
        assertEquals(expectedCottage.getCottageId(), cottage.getCottageId());
        assertEquals(expectedCottage.getCottageNumber(), cottage.getCottageNumber());
        assertEquals(expectedCottage.getCottageSize(), cottage.getCottageSize());
        assertEquals(expectedCottage.getPrice(), cottage.getPrice());
        assertEquals(expectedCottage.getDescription(), cottage.getDescription());
    }

    @Test
    void shouldMapCottageToCottageDTO() {
        //given
        Cottage cottage = testCottage();
        CottageDTO expectedDTO = testCottageDTO();
        //when
        CottageDTO cottageDTO = cottageMapper.mapToDTO(cottage);
        //then
        assertEquals(expectedDTO.cottageId(), cottageDTO.cottageId());
        assertEquals(expectedDTO.cottageNumber(), cottageDTO.cottageNumber());
        assertEquals(expectedDTO.cottageSize(), cottageDTO.cottageSize());
        assertEquals(expectedDTO.price(), cottageDTO.price());
        assertEquals(expectedDTO.description(), cottageDTO.description());
    }
}