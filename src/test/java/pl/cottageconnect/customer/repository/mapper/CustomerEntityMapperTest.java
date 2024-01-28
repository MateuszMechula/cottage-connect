package pl.cottageconnect.customer.repository.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.entity.CustomerEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.cottageconnect.util.TestDataFactoryCustomer.testCustomer;
import static pl.cottageconnect.util.TestDataFactoryCustomer.testCustomerEntity;

class CustomerEntityMapperTest {
    private final CustomerEntityMapper customerEntityMapper = Mappers.getMapper(CustomerEntityMapper.class);

    @Test
    void shouldMapCustomerToCustomerEntitySuccessfully() {
        //given
        Customer customer = testCustomer();
        //when
        CustomerEntity customerEntity = customerEntityMapper.mapToEntity(customer);
        //then
        assertEquals(customer.getCustomerId(), customerEntity.getCustomerId());
        assertEquals(customer.getFirstname(), customerEntity.getFirstname());
        assertEquals(customer.getLastname(), customerEntity.getLastname());
        assertEquals(customer.getPhone(), customerEntity.getPhone());
        assertEquals(customer.getUserId(), customerEntity.getUserId());
    }

    @Test
    void shouldMapCustomerEntityToCustomerSuccessfully() {
        //given
        CustomerEntity customerEntity = testCustomerEntity();
        //when
        Customer customer = customerEntityMapper.mapFromEntity(customerEntity);
        //then
        assertEquals(customerEntity.getCustomerId(), customer.getCustomerId());
        assertEquals(customerEntity.getFirstname(), customer.getFirstname());
        assertEquals(customerEntity.getLastname(), customer.getLastname());
        assertEquals(customerEntity.getPhone(), customer.getPhone());
        assertEquals(customerEntity.getUserId(), customer.getUserId());
    }
}