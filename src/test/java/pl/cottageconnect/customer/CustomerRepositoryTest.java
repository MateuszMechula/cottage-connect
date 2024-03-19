package pl.cottageconnect.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static pl.cottageconnect.util.TestDataFactoryCustomer.testCustomer;
import static pl.cottageconnect.util.TestDataFactoryCustomer.testCustomerEntity;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryTest {

    @Mock
    private CustomerJpaRepository customerJpaRepository;
    @Mock
    private CustomerEntityMapper customerEntityMapper;
    @InjectMocks
    private CustomerRepository customerRepository;

    @Test
    void shouldFindCustomerByUserIdSuccessfully() {
        //given
        Integer userId = 1;
        CustomerEntity customerEntity = testCustomerEntity();
        Customer customer = testCustomer();

        when(customerJpaRepository.findCustomerByUserId(userId)).thenReturn(Optional.ofNullable(customerEntity));
        when(customerEntityMapper.mapFromEntity(customerEntity)).thenReturn(customer);
        //when
        Optional<Customer> customerByUserId = customerRepository.findCustomerByUserId(userId);
        //then
        assertTrue(customerByUserId.isPresent());
        assertEquals(customer, customerByUserId.get());
    }

    @Test
    void shouldSaveCustomerSuccessfully() {
        //given
        Customer customer = testCustomer();
        CustomerEntity customerEntity = testCustomerEntity();

        when(customerEntityMapper.mapToEntity(customer)).thenReturn(customerEntity);
        when(customerJpaRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerEntityMapper.mapFromEntity(customerEntity)).thenReturn(customer);
        //when
        Customer saved = customerRepository.save(customer);
        //then
        assertEquals(customer, saved);
    }
}