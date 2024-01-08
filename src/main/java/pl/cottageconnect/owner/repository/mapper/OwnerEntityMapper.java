package pl.cottageconnect.owner.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.entity.OwnerEntity;
import pl.cottageconnect.village.repository.mapper.VillageEntityMapper;

@Mapper(componentModel = "spring", uses = {VillageEntityMapper.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OwnerEntityMapper {
    OwnerEntity mapToEntity(Owner owner);


    Owner mapFromEntity(OwnerEntity saved);
}
