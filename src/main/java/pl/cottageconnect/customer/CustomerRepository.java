package pl.cottageconnect.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class CustomerRepository implements CustomerDAO {
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerEntityMapper customerEntityMapper;

    @Override
    public Optional<Customer> findCustomerById(Long customerId) {
        return customerJpaRepository.findById(customerId)
                .map(customerEntityMapper::mapFromEntity);
    }

    @Override
    public Optional<Customer> findCustomerByUserId(Integer userId) {
        return customerJpaRepository.findCustomerByUserId(userId)
                .map(customerEntityMapper::mapFromEntity);
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity toSave = customerEntityMapper.mapToEntity(customer);
        CustomerEntity saved = customerJpaRepository.save(toSave);
        return customerEntityMapper.mapFromEntity(saved);
    }
}
