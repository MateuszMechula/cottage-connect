package pl.cottageconnect.owner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pl.cottageconnect.util.TestDataFactoryOwner.testOwner;
import static pl.cottageconnect.util.TestDataFactoryOwner.testOwnerEntity;

@ExtendWith(MockitoExtension.class)
class OwnerRepositoryTest {
    @Mock
    private OwnerJpaRepository ownerJpaRepository;
    @Mock
    private OwnerEntityMapper ownerEntityMapper;
    @InjectMocks
    private OwnerRepository ownerRepository;

    @Test
    void shouldSaveOwnerSuccessfully() {
        //given
        Owner owner = testOwner();
        OwnerEntity ownerEntity = testOwnerEntity();

        when(ownerEntityMapper.mapToEntity(owner)).thenReturn(ownerEntity);
        when(ownerJpaRepository.save(ownerEntity)).thenReturn(ownerEntity);
        when(ownerEntityMapper.mapFromEntity(ownerEntity)).thenReturn(owner);
        //when
        Owner saved = ownerRepository.save(owner);
        //then
        assertNotNull(saved);
        assertEquals(owner, saved);
    }

    @Test
    void shouldFindOwnerByUserIdSuccessfully() {
        //given
        Integer userId = 1;
        Owner owner = testOwner();
        OwnerEntity ownerEntity = testOwnerEntity();

        when(ownerJpaRepository.findOwnerByUserId(userId)).thenReturn(Optional.ofNullable(ownerEntity));
        when(ownerEntityMapper.mapFromEntity(ownerEntity)).thenReturn(owner);
        //when
        Optional<Owner> ownerByUserId = ownerRepository.findOwnerByUserId(userId);
        //then
        assertTrue(ownerByUserId.isPresent());
        assertEquals(owner, ownerByUserId.get());
    }
}