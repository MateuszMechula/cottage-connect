package pl.cottageconnect.owner.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.service.dao.OwnerDAO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static pl.cottageconnect.owner.service.OwnerService.ErrorMessages.OWNER_NOT_FOUND;
import static pl.cottageconnect.util.TestDataFactoryOwner.testOwner;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {
    @Mock
    private OwnerDAO ownerDAO;
    @InjectMocks
    private OwnerService ownerService;


    @Test
    void shouldFindOwnerByUserIdSuccessfully() {
        //given
        Integer userId = 1;
        Owner owner = testOwner();

        when(ownerDAO.findOwnerByUserId(userId)).thenReturn(Optional.ofNullable(owner));
        //when
        Owner ownerByUserId = ownerService.findOwnerByUserId(userId);
        //then
        assertEquals(owner, ownerByUserId);
    }

    @Test
    void shouldThrowExceptionWhenFindOwnerByUserId() {
        //given
        Integer userId = 1;

        when(ownerDAO.findOwnerByUserId(userId)).thenReturn(Optional.empty());
        //when, then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> ownerService.findOwnerByUserId(userId));
        assertEquals(exception.getMessage(), OWNER_NOT_FOUND.formatted(userId));
    }
}