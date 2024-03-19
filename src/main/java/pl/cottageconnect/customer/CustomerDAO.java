package pl.cottageconnect.customer;

import java.util.Optional;

interface CustomerDAO {
    Optional<Customer> findCustomerById(Long customerId);

    Optional<Customer> findCustomerByUserId(Integer userId);

    Customer save(Customer customer);
}
