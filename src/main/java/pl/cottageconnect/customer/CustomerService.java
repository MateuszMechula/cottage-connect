package pl.cottageconnect.customer;

public interface CustomerService {
    Customer save(Customer customer);

    Customer findCustomerByUserId(Integer userId);

    Customer findCustomerById(Long customerId);
}
