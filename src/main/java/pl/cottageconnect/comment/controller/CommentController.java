package pl.cottageconnect.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cottageconnect.comment.controller.dto.CommentRequestDTO;
import pl.cottageconnect.comment.controller.dto.CommentResponseDTO;
import pl.cottageconnect.comment.controller.mapper.CommentRequestMapper;
import pl.cottageconnect.comment.controller.mapper.CommentResponseMapper;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.enums.CommentableType;
import pl.cottageconnect.comment.service.CommentService;

import java.security.Principal;

import static pl.cottageconnect.comment.controller.CommentController.Routes.ADD_COMMENT_TO_ENTITY;
import static pl.cottageconnect.comment.controller.CommentController.Routes.GET_ALL_COMMENTS_BY_ENTITY_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "manage comments", description = "Endpoints responsible for comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentRequestMapper commentRequestMapper;
    private final CommentResponseMapper commentResponseMapper;


    @Operation(
            summary = "Get all comments by entity Id",
            description = "Retrieve all comments associated with the given entity Id."
    )
    @GetMapping(value = GET_ALL_COMMENTS_BY_ENTITY_ID)
    public ResponseEntity<Page<CommentResponseDTO>> getAllCommentsByEntityId(@PathVariable Long entityId,
                                                                             Principal connectedUser,
                                                                             CommentableType type,
                                                                             Pageable pageable) {
        Page<CommentResponseDTO> listOfComments = commentService.getCommentsByEntityId(entityId, type, connectedUser, pageable)
                .map(commentResponseMapper::mapToDTO);

        return ResponseEntity.ok(listOfComments);
    }

    @Operation(
            summary = "Add a new comment",
            description = "Create a new comment with the provided information."
    )
    @PostMapping(value = ADD_COMMENT_TO_ENTITY)
    public ResponseEntity<CommentResponseDTO> addCommentToEntity(
            @PathVariable Long entityId,
            @RequestBody CommentRequestDTO commentRequestDTO,
            Principal connectedUser,
            CommentableType type) {

        Comment commentToSave = commentRequestMapper.map(commentRequestDTO);
        Comment savedComment = commentService.addCommentToEntity(entityId, type, commentToSave, connectedUser);
        CommentResponseDTO commentResponseDTO = commentResponseMapper.mapToDTO(savedComment);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
    }

    static final class Routes {
        static final String ROOT = "/api/v1";
        static final String ENTITIES = "/entities";
        static final String COMMENTS = "/comments";
        static final String ADD_COMMENT_TO_ENTITY = ROOT + ENTITIES + "/{entityId}" + COMMENTS;
        static final String GET_ALL_COMMENTS_BY_ENTITY_ID = ROOT + ENTITIES + "/{entityId}" + COMMENTS;
    }
}
