package pl.cottageconnect.comment.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.comment.controller.dto.CommentRequestDTO;
import pl.cottageconnect.comment.domain.Comment;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CommentRequestMapper {

    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "commentableId", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "user", ignore = true)
    Comment map(CommentRequestDTO commentRequestDTO);
}
