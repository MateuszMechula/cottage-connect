package pl.cottageconnect.customer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.service.dao.CustomerDAO;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerDAO customerDAO;

    @Transactional
    public void saveCustomer(Customer customer) {
        customerDAO.save(customer);
    }
}
