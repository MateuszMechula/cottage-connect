package pl.cottageconnect.village.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.entity.VillageEntity;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VillageEntityMapper {

    @Mapping(target = "posts", ignore = true)
    VillageEntity mapToEntity(Village village);

    @Mapping(target = "posts", ignore = true)
    Village mapFromEntity(VillageEntity saved);
}
