package pl.cottageconnect.village.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.address.AddressMapper;
import pl.cottageconnect.village.VillagePost;
import pl.cottageconnect.village.controller.dto.VillagePostRequestDTO;

@Mapper(componentModel = "spring", uses = AddressMapper.class, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VillagePostRequestMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "village", ignore = true)
    @Mapping(target = "villagePostId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    VillagePost map(VillagePostRequestDTO villagePostRequestDTO);
}
