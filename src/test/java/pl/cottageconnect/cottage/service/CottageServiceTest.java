package pl.cottageconnect.cottage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.common.exception.exceptions.UnauthorizedAccessException;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.service.dao.CottageDAO;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.service.CustomerService;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.security.domain.Role;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.service.VillageService;

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
class CottageServiceTest {

    @Mock
    private CottageDAO cottageDAO;
    @Mock
    private VillageService villageService;
    @Mock
    private UserService userService;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private CottageService cottageService;

    @Test
    void shouldGetCottageWithCheckSuccessfully() {
        //given
        Long cottageId = 1L;
        Integer customerId = 2;
        Principal connectedUser = mock(Principal.class);
        Customer customer = testCustomer().withUserId(2);

        User user = testUser().withUserId(2).withRoles(Set.of(Role.builder().role(ROLE_CUSTOMER).build()));
        Owner owner = testOwner().withUserId(1);
        Village village = testVillage().withOwner(owner);
        Cottage expectedCottage = testCottage().withVillage(village);

        when(cottageDAO.getCottage(cottageId)).thenReturn(Optional.of(expectedCottage));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        when(customerService.findCustomerByUserId(customerId)).thenReturn(customer);
        //when
        Cottage cottage = cottageService.getCottageWithCheck(cottageId, connectedUser);
        //then
        assertEquals(expectedCottage, cottage);
    }

    @Test
    void whenGetCottageWithCheckShouldThrowUnauthorizedAccessException() {
        //given
        Long cottageId = 1L;
        Principal connectedUser = mock(Principal.class);
        Owner owner = testOwner();
        User user = testUser().withUserId(2);
        Village village = testVillage().withOwner(owner);
        Cottage expectedCottage = testCottage().withVillage(village);

        when(cottageDAO.getCottage(cottageId)).thenReturn(Optional.of(expectedCottage));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        //when,then
        assertThrows(UnauthorizedAccessException.class, () -> cottageService.getCottageWithCheck(cottageId, connectedUser));
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
        List<Cottage> allCottages = cottageService.findAllCottages(villageId, connectedUser);
        //then
        assertEquals(expectedCottages, allCottages);
    }

    @Test
    void shouldUpdateCottageSuccessfully() {
        //given
        Long cottageId = 1L;
        Cottage toUpdate = testCottage().withCottageId(cottageId);
        Principal connectedUser = mock(Principal.class);

        Owner owner = testOwner();
        User user = testUser().withUserId(1);
        Village village = testVillage().withOwner(owner);
        Cottage existingCottage = testCottage().withVillage(village);

        when(cottageDAO.getCottage(cottageId)).thenReturn(Optional.ofNullable(existingCottage));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        when(cottageDAO.addCottage(existingCottage)).thenReturn(existingCottage);
        //when
        Cottage updatedCottage = cottageService.updateCottage(cottageId, toUpdate, connectedUser);
        //then
        assertEquals(toUpdate, updatedCottage);
    }

    @Test
    void shouldAddCottageSuccessfully() {
        //given
        Long villageId = 1L;
        Cottage cottage = testCottage();
        Village village = testVillage();
        Principal connectedUser = mock(Principal.class);


        when(villageService.getVillage(villageId, connectedUser)).thenReturn(village);
        when(cottageDAO.addCottage(cottage)).thenReturn(cottage);
        //when
        cottageService.addCottage(villageId, cottage, connectedUser);
        //then
        verify(cottageDAO, times(1)).addCottage(cottage);
    }

    @Test
    void shouldDeleteCottageSuccessfully() {
        //given
        Long cottageId = 1L;
        Owner owner = testOwner();
        Village village = testVillage().withOwner(owner.withUserId(1));
        Cottage cottage = testCottage().withVillage(village);
        User user = testUser().withUserId(1);
        Principal connectedUser = mock(Principal.class);

        when(cottageDAO.getCottage(cottageId)).thenReturn(Optional.ofNullable(cottage));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        doNothing().when(cottageDAO).deleteCottage(cottageId);
        //when
        cottageService.deleteCottage(cottageId, connectedUser);
        //then
        verify(cottageDAO, times(1)).deleteCottage(cottageId);
    }
}