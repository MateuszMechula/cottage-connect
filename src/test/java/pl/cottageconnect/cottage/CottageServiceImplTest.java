package pl.cottageconnect.cottage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.common.exception.exceptions.UnauthorizedAccessException;
import pl.cottageconnect.customer.Customer;
import pl.cottageconnect.customer.CustomerService;
import pl.cottageconnect.owner.Owner;
import pl.cottageconnect.security.Role;
import pl.cottageconnect.security.User;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.village.Village;
import pl.cottageconnect.village.VillageService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryCottage.*;
import static pl.cottageconnect.util.TestDataFactoryOwner.testOwner;
import static pl.cottageconnect.util.TestDataFactoryUser.*;
import static pl.cottageconnect.village.util.TestDataFactoryVillage.testVillage;

@ExtendWith(MockitoExtension.class)
class CottageServiceImplTest {

    @Mock
    private CottageDAO cottageDAO;
    @Mock
    private VillageService villageService;
    @Mock
    private UserService userService;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private CottageServiceImpl cottageServiceImpl;

    @Test
    void shouldGetCottageWithCheckSuccessfully() {
        //given
        Long cottageId = 1L;
        Integer customerId = 2;
        Principal connectedUser = mock(Principal.class);
        Customer customer = testCustomer().withUserId(2);

        User user = testUserRoleCustomer().withUserId(2).withRoles(Set.of(Role.builder().role(ROLE_CUSTOMER).build()));
        Owner owner = testOwner().withUserId(1);
        Village village = testVillage().withOwner(owner);
        Cottage expectedCottage = testCottage().withVillage(village);

        when(cottageDAO.getCottage(cottageId)).thenReturn(Optional.of(expectedCottage));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        when(customerService.findCustomerByUserId(customerId)).thenReturn(customer);
        //when
        Cottage cottage = cottageServiceImpl.getCottageWithCheck(cottageId, connectedUser);
        //then
        assertEquals(expectedCottage, cottage);
    }

    @Test
    void whenGetCottageWithCheckShouldThrowUnauthorizedAccessException() {
        //given
        Long cottageId = 1L;
        Principal connectedUser = mock(Principal.class);
        Owner owner = testOwner();
        User user = testUserRoleCustomer().withUserId(2);
        Village village = testVillage().withOwner(owner);
        Cottage expectedCottage = testCottage().withVillage(village);

        when(cottageDAO.getCottage(cottageId)).thenReturn(Optional.of(expectedCottage));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        //when,then
        assertThrows(UnauthorizedAccessException.class, () -> cottageServiceImpl.getCottageWithCheck(cottageId, connectedUser));
    }

    @Test
    void shouldFindAllCottagesSuccessfully() {
        //given
        Long villageId = 1L;
        Principal connectedUser = mock(Principal.class);
        Set<Cottage> cottages = Set.of(testCottage2(), testCottage3());
        List<Cottage> expectedCottages = List.of(testCottage2(), testCottage3());
        Village village = testVillage().withCottages(cottages);

        when(villageService.getVillage(villageId, connectedUser)).thenReturn(village);
        //when
        List<Cottage> allCottages = cottageServiceImpl.findAllCottages(villageId, connectedUser);
        //then
        assertEquals(expectedCottages.size(), allCottages.size());
    }

    @Test
    void shouldUpdateCottageSuccessfully() {
        //given
        Long cottageId = 1L;
        Cottage toUpdate = testCottage().withCottageId(cottageId);
        Principal connectedUser = mock(Principal.class);

        Owner owner = testOwner();
        User user = testUserRoleCustomer().withUserId(1);
        Village village = testVillage().withOwner(owner);
        Cottage existingCottage = testCottage().withVillage(village);

        when(cottageDAO.getCottage(cottageId)).thenReturn(Optional.ofNullable(existingCottage));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        when(cottageDAO.addCottage(existingCottage)).thenReturn(existingCottage);
        //when
        Cottage updatedCottage = cottageServiceImpl.updateCottage(cottageId, toUpdate, connectedUser);
        //then
        assertEquals(existingCottage, updatedCottage);
    }

    @Test
    void shouldAddCottageSuccessfully() {
        //given
        Long villageId = 1L;
        Cottage cottage = testCottage();
        Village village = testVillage();
        Cottage cottageWithVillage = cottage.withVillage(village);
        Principal connectedUser = mock(Principal.class);


        when(villageService.getVillage(villageId, connectedUser)).thenReturn(village);
        when(cottageDAO.addCottage(cottageWithVillage)).thenReturn(cottageWithVillage);
        //when
        cottageServiceImpl.addCottage(villageId, cottage, connectedUser);
        //then
        verify(cottageDAO, times(1)).addCottage(cottageWithVillage);
    }

    @Test
    void shouldDeleteCottageSuccessfully() {
        //given
        Long cottageId = 1L;
        Owner owner = testOwner();
        Village village = testVillage().withOwner(owner.withUserId(1));
        Cottage cottage = testCottage().withVillage(village);
        User user = testUserRoleCustomer().withUserId(1);
        Principal connectedUser = mock(Principal.class);

        when(cottageDAO.getCottage(cottageId)).thenReturn(Optional.ofNullable(cottage));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        doNothing().when(cottageDAO).deleteCottage(cottageId);
        //when
        cottageServiceImpl.deleteCottage(cottageId, connectedUser);
        //then
        verify(cottageDAO, times(1)).deleteCottage(cottageId);
    }
}