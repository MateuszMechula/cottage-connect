package pl.cottageconnect.customer;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CustomerEntityMapper {
    CustomerEntity mapToEntity(Customer customer);

    Customer mapFromEntity(CustomerEntity saved);
}
