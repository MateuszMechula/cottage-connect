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
    @Mapping(target = "villageId", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "cottages", ignore = true)
    @Mapping(target = "posts", ignore = true)
    Village map(VillageDTO villageDTO);

    @Mapping(source = "address", target = "addressDTO")
    VillageDTO mapToDTO(Village village);
}
