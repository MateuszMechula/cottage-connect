package pl.cottageconnect.like.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.like.domain.Like;
import pl.cottageconnect.like.entity.LikeEntity;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LikeEntityMapper {
    @Mapping(target = "user.likes", ignore = true)
    @Mapping(target = "user.posts", ignore = true)
    @Mapping(target = "user.comments", ignore = true)
    LikeEntity mapToEntity(Like like);

    Like mapFromEntity(LikeEntity entity);
}
