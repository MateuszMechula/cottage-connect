package pl.cottageconnect.cottage.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.cottage.Cottage;
import pl.cottageconnect.cottage.controller.dto.CottageDTO;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CottageMapper {
    @Mapping(target = "village", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Cottage map(CottageDTO cottageDTO);

    CottageDTO mapToDTO(Cottage cottage);
}
