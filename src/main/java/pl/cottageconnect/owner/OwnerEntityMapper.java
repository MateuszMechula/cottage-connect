package pl.cottageconnect.owner;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.village.VillageEntityMapper;

@Mapper(componentModel = "spring", uses = {VillageEntityMapper.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OwnerEntityMapper {
    OwnerEntity mapToEntity(Owner owner);


    Owner mapFromEntity(OwnerEntity saved);
}
