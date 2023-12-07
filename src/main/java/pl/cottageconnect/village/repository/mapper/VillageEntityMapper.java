package pl.cottageconnect.village.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.entity.VillageEntity;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VillageEntityMapper {
    VillageEntity mapToEntity(Village village);

    Village mapFromEntity(VillageEntity saved);
}
