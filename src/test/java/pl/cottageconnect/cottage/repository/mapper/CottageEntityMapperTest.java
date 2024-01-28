package pl.cottageconnect.cottage.repository.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.entity.CottageEntity;

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
        assertEquals(cottage.getCottageId(), entity.getCottageId());
        assertEquals(cottage.getCottageNumber(), entity.getCottageNumber());
        assertEquals(cottage.getCottageSize(), entity.getCottageSize());
        assertEquals(cottage.getPrice(), entity.getPrice());
        assertEquals(cottage.getDescription(), entity.getDescription());
    }

    @Test
    void shouldMapCottageFromEntity() {
        //given
        CottageEntity entity = testCottageEntity();
        //when
        Cottage cottage = cottageEntityMapper.mapFromEntity(entity);
        //then
        assertEquals(entity.getCottageId(), cottage.getCottageId());
        assertEquals(entity.getCottageNumber(), cottage.getCottageNumber());
        assertEquals(entity.getCottageSize(), cottage.getCottageSize());
        assertEquals(entity.getPrice(), cottage.getPrice());
        assertEquals(entity.getDescription(), cottage.getDescription());
    }
}