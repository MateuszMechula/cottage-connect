package pl.cottageconnect.customer.service.dao;

import pl.cottageconnect.customer.domain.Customer;

public interface CustomerDAO {
    Customer save(Customer customer);
}
