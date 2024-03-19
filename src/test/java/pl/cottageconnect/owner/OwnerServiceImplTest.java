package pl.cottageconnect.owner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static pl.cottageconnect.owner.OwnerServiceImpl.ErrorMessages.OWNER_NOT_FOUND;
import static pl.cottageconnect.util.TestDataFactoryOwner.testOwner;

@ExtendWith(MockitoExtension.class)
class OwnerServiceImplTest {
    @Mock
    private OwnerDAO ownerDAO;
    @InjectMocks
    private OwnerServiceImpl ownerServiceImpl;


    @Test
    void shouldFindOwnerByUserIdSuccessfully() {
        //given
        Integer userId = 1;
        Owner owner = testOwner();

        when(ownerDAO.findOwnerByUserId(userId)).thenReturn(Optional.ofNullable(owner));
        //when
        Owner ownerByUserId = ownerServiceImpl.findOwnerByUserId(userId);
        //then
        assertEquals(owner, ownerByUserId);
    }

    @Test
    void shouldThrowExceptionWhenFindOwnerByUserId() {
        //given
        Integer userId = 1;

        when(ownerDAO.findOwnerByUserId(userId)).thenReturn(Optional.empty());
        //when, then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> ownerServiceImpl.findOwnerByUserId(userId));
        assertEquals(exception.getMessage(), OWNER_NOT_FOUND.formatted(userId));
    }
}