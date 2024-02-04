package pl.cottageconnect.owner.repository.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.entity.OwnerEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.util.TestDataFactoryOwner.testOwner;
import static pl.cottageconnect.util.TestDataFactoryOwner.testOwnerEntity;

class OwnerEntityMapperTest {

    private final OwnerEntityMapper ownerEntityMapper = Mappers.getMapper(OwnerEntityMapper.class);

    @Test
    void shouldMapOwnerToOwnerEntitySuccessfully() {
        //given
        Owner owner = testOwner();

        //when
        OwnerEntity entity = ownerEntityMapper.mapToEntity(owner);
        //then
        assertEquals(owner.getOwnerId(), entity.getOwnerId());
        assertEquals(owner.getFirstname(), entity.getFirstname());
        assertEquals(owner.getLastname(), entity.getLastname());
        assertEquals(owner.getPhone(), entity.getPhone());
        assertEquals(owner.getUserId(), entity.getUserId());
    }

    @Test
    void shouldMapOwnerEntityToOwnerSuccessfully() {
        //given
        OwnerEntity ownerEntity = testOwnerEntity();
        //when
        Owner owner = ownerEntityMapper.mapFromEntity(ownerEntity);
        //then
        assertEquals(ownerEntity.getOwnerId(), owner.getOwnerId());
        assertEquals(ownerEntity.getFirstname(), owner.getFirstname());
        assertEquals(ownerEntity.getLastname(), owner.getLastname());
        assertEquals(ownerEntity.getPhone(), owner.getPhone());
        assertEquals(ownerEntity.getUserId(), owner.getUserId());
    }
}