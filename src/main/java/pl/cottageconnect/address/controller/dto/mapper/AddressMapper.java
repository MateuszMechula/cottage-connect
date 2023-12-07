
package pl.cottageconnect.address.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.address.controller.dto.AddressDTO;
import pl.cottageconnect.address.domain.Address;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    Address map(AddressDTO addressDTO);

    AddressDTO mapToDTO(Address address);
}
