
package pl.cottageconnect.address.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.address.controller.dto.AddressDTO;
import pl.cottageconnect.address.domain.Address;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "addressId", ignore = true)
    @Mapping(target = "village", ignore = true)
    Address map(AddressDTO addressDTO);
}
