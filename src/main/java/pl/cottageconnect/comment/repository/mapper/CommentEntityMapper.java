package pl.cottageconnect.comment.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.entity.CommentEntity;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CommentEntityMapper {

    @Mapping(target = "user.likes", ignore = true)
    @Mapping(target = "user.posts", ignore = true)
    @Mapping(target = "user.comments", ignore = true)
    CommentEntity mapToEntity(Comment newComment);

    Comment mapFromEntity(CommentEntity savedEntity);
}
