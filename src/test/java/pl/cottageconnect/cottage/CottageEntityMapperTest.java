package pl.cottageconnect.cottage;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.util.TestDataFactoryCottage.testCottage;
import static pl.cottageconnect.util.TestDataFactoryCottage.testCottageEntity;

class CottageEntityMapperTest {

    private final CottageEntityMapper cottageEntityMapper = Mappers.getMapper(CottageEntityMapper.class);

    @Test
    void shouldMapCottageToEntitySuccessfully() {
        //given
        Cottage cottage = testCottage();
        //when
        CottageEntity entity = cottageEntityMapper.mapToEntity(cottage);
        //then
        assertEquals(cottage.cottageId(), entity.getCottageId());
        assertEquals(cottage.cottageNumber(), entity.getCottageNumber());
        assertEquals(cottage.cottageSize(), entity.getCottageSize());
        assertEquals(cottage.price(), entity.getPrice());
        assertEquals(cottage.description(), entity.getDescription());
    }

    @Test
    void shouldMapCottageFromEntity() {
        //given
        CottageEntity entity = testCottageEntity();
        //when
        Cottage cottage = cottageEntityMapper.mapFromEntity(entity);
        //then
        assertEquals(entity.getCottageId(), cottage.cottageId());
        assertEquals(entity.getCottageNumber(), cottage.cottageNumber());
        assertEquals(entity.getCottageSize(), cottage.cottageSize());
        assertEquals(entity.getPrice(), cottage.price());
        assertEquals(entity.getDescription(), cottage.description());
    }
}