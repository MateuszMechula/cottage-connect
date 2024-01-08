package pl.cottageconnect.village.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.cottage.repository.mapper.CottageEntityMapper;
import pl.cottageconnect.security.repository.mapper.UserEntityMapper;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.entity.VillageEntity;

@Mapper(componentModel = "spring", uses = {CottageEntityMapper.class, VillagePostEntityMapper.class, UserEntityMapper.class}
        , unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VillageEntityMapper {

    @Mapping(target = "posts.village", ignore = true)
    VillageEntity mapToEntity(Village village);

    @Mapping(target = "posts.village", ignore = true)
    @Mapping(target = "owner.village", ignore = true)
    @Mapping(target = "address.village", ignore = true)
    Village mapFromEntity(VillageEntity saved);
}
