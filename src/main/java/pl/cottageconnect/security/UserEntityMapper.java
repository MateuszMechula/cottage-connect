package pl.cottageconnect.security;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    User mapFromEntity(UserEntity entity);

    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "comments", ignore = true)
    UserEntity mapToEntity(User user);
}
