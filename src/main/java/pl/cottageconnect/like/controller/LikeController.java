package pl.cottageconnect.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cottageconnect.like.controller.dto.LikeDTO;
import pl.cottageconnect.like.controller.dto.mapper.LikeMapper;
import pl.cottageconnect.like.domain.Like;
import pl.cottageconnect.like.enums.LikeableType;
import pl.cottageconnect.like.service.LikeService;

import java.security.Principal;

import static pl.cottageconnect.like.controller.LikeController.Routes.ADD_LIKE;
import static pl.cottageconnect.like.controller.LikeController.Routes.DELETE_LIKE;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "likes", description = "Endpoints responsible for likes (<b>OWNER</b>, <b>CUSTOMER</b>)")
public class LikeController {

    private final LikeService likeService;
    private final LikeMapper likeMapper;

    @Operation(
            summary = "Add like for types: COMMENT, VILLAGE POST by ID",
            description = "You can add like to COMMENT or VILLAGE POST by ID (likeableId = commentId or villagePostId)"
    )
    @PostMapping(value = ADD_LIKE)
    public ResponseEntity<LikeDTO> addLike(
            @RequestParam(name = "likeableId") Long likeableId,
            @RequestParam(name = "type") LikeableType type,
            Principal connectedUser) {

        Like like = likeService.addLike(likeableId, type, connectedUser);
        LikeDTO likeDTO = likeMapper.mapToDTO(like);
        return ResponseEntity.ok(likeDTO);
    }

    @Operation(
            summary = "Delete like by ID",
            description = "You can delete like by its unique ID)"
    )
    @DeleteMapping(value = DELETE_LIKE)
    public ResponseEntity<Void> deleteLike(@PathVariable Long likeId, Principal connectedUser) {
        likeService.deleteLike(likeId, connectedUser);
        return ResponseEntity.noContent().build();
    }

    static final class Routes {
        static final String ROOT = "/api/v1/likes";
        static final String ADD_LIKE = ROOT;
        static final String DELETE_LIKE = ROOT + "/{likeId}";
    }
}
