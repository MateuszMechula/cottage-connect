package pl.cottageconnect.customer.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.entity.CustomerEntity;
import pl.cottageconnect.customer.repository.jpa.CustomerJpaRepository;
import pl.cottageconnect.customer.repository.mapper.CustomerEntityMapper;
import pl.cottageconnect.customer.service.dao.CustomerDAO;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepository implements CustomerDAO {
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
