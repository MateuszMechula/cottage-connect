package pl.cottageconnect.cottage.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.entity.CottageEntity;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CottageEntityMapper {
    @Mapping(target = "village.cottages", ignore = true)
    @Mapping(target = "village.posts", ignore = true)
    CottageEntity mapToEntity(Cottage cottage);

    @Mapping(target = "village.cottages", ignore = true)
    @Mapping(target = "village.posts", ignore = true)
    Cottage mapFromEntity(CottageEntity cottage);

}
