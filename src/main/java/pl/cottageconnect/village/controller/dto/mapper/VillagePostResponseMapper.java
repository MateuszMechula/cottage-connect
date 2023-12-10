package pl.cottageconnect.village.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.address.controller.dto.mapper.AddressMapper;
import pl.cottageconnect.village.controller.dto.VillagePostResponseDTO;
import pl.cottageconnect.village.domain.VillagePost;

@Mapper(componentModel = "spring", uses = AddressMapper.class, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VillagePostResponseMapper {
    @Mapping(target = "village", ignore = true)
    @Mapping(target = "user", ignore = true)
    VillagePost map(VillagePostResponseDTO villagePostResponseDTO);

    VillagePostResponseDTO mapToDTO(VillagePost villagePost);
}
