package pl.cottageconnect.like.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.like.Like;
import pl.cottageconnect.like.controller.dto.LikeDTO;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {

    @Mapping(target = "userId", source = "user.userId")
    LikeDTO mapToDTO(Like like);
}
