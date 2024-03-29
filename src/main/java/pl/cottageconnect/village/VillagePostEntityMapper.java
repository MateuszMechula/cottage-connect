package pl.cottageconnect.village;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VillagePostEntityMapper {

    @Mapping(target = "user.likes", ignore = true)
    @Mapping(target = "user.posts", ignore = true)
    @Mapping(target = "user.comments", ignore = true)
    VillagePostEntity mapToEntity(VillagePost village);

    @Mapping(target = "village.owner", ignore = true)
    @Mapping(target = "village.address", ignore = true)
    @Mapping(target = "village.cottages", ignore = true)
    @Mapping(target = "village.posts", ignore = true)
    VillagePost mapFromEntity(VillagePostEntity saved);
}
