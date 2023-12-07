package pl.cottageconnect.village.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.address.controller.dto.mapper.AddressMapper;
import pl.cottageconnect.village.controller.dto.VillageDTO;
import pl.cottageconnect.village.domain.Village;

@Mapper(componentModel = "spring", uses = AddressMapper.class, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VillageMapper {

    @Mapping(source = "addressDTO", target = "address")
    Village map(VillageDTO villageDTO);

    @Mapping(source = "address", target = "addressDTO")
    VillageDTO mapToDTO(Village village);
}
