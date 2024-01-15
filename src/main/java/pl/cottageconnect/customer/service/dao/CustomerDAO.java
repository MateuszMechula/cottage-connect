package pl.cottageconnect.customer.service.dao;

import pl.cottageconnect.customer.domain.Customer;

import java.util.Optional;

public interface CustomerDAO {
    Optional<Customer> findCustomerById(Long customerId);

    Optional<Customer> findCustomerByUserId(Integer userId);

    Customer save(Customer customer);
}
