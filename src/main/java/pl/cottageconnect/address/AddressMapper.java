
package pl.cottageconnect.address;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "addressId", ignore = true)
    @Mapping(target = "village", ignore = true)
    Address map(AddressDTO addressDTO);
}
