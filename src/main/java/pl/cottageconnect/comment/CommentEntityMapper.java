package pl.cottageconnect.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CommentEntityMapper {

    @Mapping(target = "user.likes", ignore = true)
    @Mapping(target = "user.posts", ignore = true)
    @Mapping(target = "user.comments", ignore = true)
    CommentEntity mapToEntity(Comment newComment);

    Comment mapFromEntity(CommentEntity savedEntity);
}
