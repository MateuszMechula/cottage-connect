package pl.cottageconnect.customer;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

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
        assertEquals(customer.customerId(), customerEntity.getCustomerId());
        assertEquals(customer.firstname(), customerEntity.getFirstname());
        assertEquals(customer.lastname(), customerEntity.getLastname());
        assertEquals(customer.phone(), customerEntity.getPhone());
        assertEquals(customer.userId(), customerEntity.getUserId());
    }

    @Test
    void shouldMapCustomerEntityToCustomerSuccessfully() {
        //given
        CustomerEntity customerEntity = testCustomerEntity();
        //when
        Customer customer = customerEntityMapper.mapFromEntity(customerEntity);
        //then
        assertEquals(customerEntity.getCustomerId(), customer.customerId());
        assertEquals(customerEntity.getFirstname(), customer.firstname());
        assertEquals(customerEntity.getLastname(), customer.lastname());
        assertEquals(customerEntity.getPhone(), customer.phone());
        assertEquals(customerEntity.getUserId(), customer.userId());
    }
}