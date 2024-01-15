package pl.cottageconnect.village.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.village.domain.VillagePost;
import pl.cottageconnect.village.entity.VillagePostEntity;

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
