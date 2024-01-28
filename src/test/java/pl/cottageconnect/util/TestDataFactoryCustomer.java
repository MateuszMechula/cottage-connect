package pl.cottageconnect.util;

import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.entity.CustomerEntity;

public class TestDataFactoryCustomer {


    public static Customer testCustomer() {
        return Customer.builder()
                .customerId(1L)
                .firstname("Jan")
                .lastname("Kowalski")
                .phone("505404330")
                .userId(1)
                .build();
    }

    public static CustomerEntity testCustomerEntity() {
        return CustomerEntity.builder()
                .customerId(1L)
                .firstname("Jan")
                .lastname("Kowalski")
                .phone("505404330")
                .userId(1)
                .build();
    }
}
