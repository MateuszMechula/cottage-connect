package pl.cottageconnect.customer.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.entity.CustomerEntity;
import pl.cottageconnect.customer.repository.jpa.CustomerJpaRepository;
import pl.cottageconnect.customer.repository.mapper.CustomerEntityMapper;

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
    void shouldFindCustomerByIdSuccessfully() {
        //given
        Long customerId = 1L;
        CustomerEntity customerEntity = testCustomerEntity();
        Customer customer = testCustomer();

        when(customerJpaRepository.findById(customerId)).thenReturn(Optional.ofNullable(customerEntity));
        when(customerEntityMapper.mapFromEntity(customerEntity)).thenReturn(customer);
        //when
        Optional<Customer> customerById = customerRepository.findCustomerById(customerId);
        //then
        assertTrue(customerById.isPresent());
        assertEquals(customer, customerById.get());
    }

    @Test
    void shouldFindCustomerByUserIdSuccessfully() {
        //given
        Integer userId = 1;
        Long customerId = 1L;
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