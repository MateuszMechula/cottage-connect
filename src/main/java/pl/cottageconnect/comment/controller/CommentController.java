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
import pl.cottageconnect.comment.Comment;
import pl.cottageconnect.comment.CommentService;
import pl.cottageconnect.comment.CommentableType;
import pl.cottageconnect.comment.controller.dto.CommentRequestDTO;
import pl.cottageconnect.comment.controller.dto.CommentResponseDTO;
import pl.cottageconnect.comment.controller.mapper.CommentRequestMapper;
import pl.cottageconnect.comment.controller.mapper.CommentResponseMapper;

import java.security.Principal;

import static pl.cottageconnect.comment.controller.CommentController.Routes.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "comments", description = "Endpoints responsible for comments (<b>OWNER</b>, <b>CUSTOMER</b>)")
public class CommentController {

    private final CommentService commentService;
    private final CommentRequestMapper commentRequestMapper;
    private final CommentResponseMapper commentResponseMapper;


    @Operation(
            summary = "Get all comments by commentable Id (VILLAGE,COTTAGE)",
            description = "Retrieve all comments associated with the given commentable Id."
    )
    @GetMapping(value = GET_ALL_COMMENTS_BY_COMMENTABLE_ID)
    public ResponseEntity<Page<CommentResponseDTO>> getAllComments(@RequestParam(name = "commentableId") Long commentableId,
                                                                   @RequestParam(name = "type") CommentableType type,
                                                                   Principal connectedUser,
                                                                   Pageable pageable) {

        Page<CommentResponseDTO> listOfComments = commentService.getCommentsByCommentableId(commentableId, type,
                connectedUser, pageable).map(commentResponseMapper::mapToDTO);
        return ResponseEntity.ok(listOfComments);
    }

    @Operation(
            summary = "Add a new comment to commentable (VILLAGE,COTTAGE)",
            description = "Create a new comment with the provided information."
    )
    @PostMapping(value = ADD_COMMENT)
    public ResponseEntity<CommentResponseDTO> addComment(
            @RequestParam("commentableId") Long commentableId,
            @RequestParam("type") CommentableType type,
            @RequestBody CommentRequestDTO commentRequestDTO,
            Principal connectedUser) {

        Comment commentToSave = commentRequestMapper.map(commentRequestDTO);
        Comment savedComment = commentService.addCommentToCommentable(commentableId, type, commentToSave, connectedUser);
        CommentResponseDTO commentResponseDTO = commentResponseMapper.mapToDTO(savedComment);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
    }

    @Operation(
            summary = "Update comment by ID",
            description = "Update comment with the provided information"
    )
    @PatchMapping(value = UPDATE_COMMENT_BY_ID)
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDTO commentRequestDTO,
            Principal connectedUser
    ) {
        Comment commentToUpdate = commentRequestMapper.map(commentRequestDTO);
        Comment updatedComment = commentService.updateComment(commentId, commentToUpdate, connectedUser);
        CommentResponseDTO commentResponseDTO = commentResponseMapper.mapToDTO(updatedComment);

        return ResponseEntity.ok(commentResponseDTO);
    }

    @Operation(
            summary = "Delete comment by ID",
            description = "Delete comment based on its unique ID"
    )
    @DeleteMapping(value = DELETE_COMMENT_BY_ID)
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Principal connectedUser) {
        commentService.deleteCommentById(commentId, connectedUser);

        return ResponseEntity.noContent().build();
    }

    static final class Routes {
        static final String ROOT = "/api/v1/comments";
        static final String ADD_COMMENT = ROOT;
        static final String UPDATE_COMMENT_BY_ID = ROOT + "/{commentId}";
        static final String DELETE_COMMENT_BY_ID = ROOT + "/{commentId}";
        static final String GET_ALL_COMMENTS_BY_COMMENTABLE_ID = ROOT;
    }
}
