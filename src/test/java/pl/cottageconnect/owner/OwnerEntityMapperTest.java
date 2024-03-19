package pl.cottageconnect.owner;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

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
        assertEquals(owner.ownerId(), entity.getOwnerId());
        assertEquals(owner.firstname(), entity.getFirstname());
        assertEquals(owner.lastname(), entity.getLastname());
        assertEquals(owner.phone(), entity.getPhone());
        assertEquals(owner.userId(), entity.getUserId());
    }

    @Test
    void shouldMapOwnerEntityToOwnerSuccessfully() {
        //given
        OwnerEntity ownerEntity = testOwnerEntity();
        //when
        Owner owner = ownerEntityMapper.mapFromEntity(ownerEntity);
        //then
        assertEquals(ownerEntity.getOwnerId(), owner.ownerId());
        assertEquals(ownerEntity.getFirstname(), owner.firstname());
        assertEquals(ownerEntity.getLastname(), owner.lastname());
        assertEquals(ownerEntity.getPhone(), owner.phone());
        assertEquals(ownerEntity.getUserId(), owner.userId());
    }
}