package pl.cottageconnect.customer.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.entity.CustomerEntity;
import pl.cottageconnect.customer.repository.jpa.CustomerJpaRepository;
import pl.cottageconnect.customer.repository.mapper.CustomerEntityMapper;
import pl.cottageconnect.customer.service.dao.CustomerDAO;

@Repository
@RequiredArgsConstructor
public class CustomerRepository implements CustomerDAO {
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerEntityMapper customerEntityMapper;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity toSave = customerEntityMapper.mapToEntity(customer);
        CustomerEntity saved = customerJpaRepository.save(toSave);
        return customerEntityMapper.mapFromEntity(saved);
    }
}
