package pl.cottageconnect.cottage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.reservation.ReservationEntityMapper;

@Mapper(componentModel = "spring", uses = {ReservationEntityMapper.class}, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CottageEntityMapper {
    @Mapping(target = "village.cottages", ignore = true)
    @Mapping(target = "village.posts", ignore = true)
    CottageEntity mapToEntity(Cottage cottage);

    @Mapping(target = "village.cottages", ignore = true)
    @Mapping(target = "village.posts", ignore = true)
    @Mapping(target = "village.owner.village", ignore = true)
    @Mapping(target = "village.address.village", ignore = true)
    Cottage mapFromEntity(CottageEntity cottage);
}
