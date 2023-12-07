package pl.cottageconnect.customer.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.customer.domain.Customer;
import pl.cottageconnect.customer.entity.CustomerEntity;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CustomerEntityMapper {
    CustomerEntity mapToEntity(Customer customer);

    Customer mapFromEntity(CustomerEntity saved);
}
