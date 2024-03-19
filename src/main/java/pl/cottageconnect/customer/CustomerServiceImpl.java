package pl.cottageconnect.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;

import static pl.cottageconnect.customer.CustomerServiceImpl.ErrorMessages.CUSTOMER_NOT_FOUND;
import static pl.cottageconnect.customer.CustomerServiceImpl.ErrorMessages.DATE_VALIDATION_ERROR;

@Service
@RequiredArgsConstructor
class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

    public Customer findCustomerById(Long customerId) {
        return customerDAO.findCustomerById(customerId)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND.formatted(customerId)));
    }

    public Customer findCustomerByUserId(Integer userId) {
        return customerDAO.findCustomerByUserId(userId)
                .orElseThrow(() -> new NotFoundException(DATE_VALIDATION_ERROR.formatted(userId)));
    }

    @Override
    public Customer save(Customer customer) {
        return customerDAO.save(customer);
    }


    static final class ErrorMessages {
        static final String DATE_VALIDATION_ERROR = "Customer with user ID: [%s] not found or you dont have access";
        static final String CUSTOMER_NOT_FOUND = "Customer with ID: [%s] not found or you dont have access";
    }
}
