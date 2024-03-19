package pl.cottageconnect.comment.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.cottageconnect.comment.Comment;
import pl.cottageconnect.comment.controller.dto.CommentResponseDTO;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CommentResponseMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(target = "user.password", ignore = true)
    @Mapping(target = "user.roles", ignore = true)
    CommentResponseDTO mapToDTO(Comment savedComment);
}
