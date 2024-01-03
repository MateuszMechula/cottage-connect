package pl.cottageconnect.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.enums.CommentableType;
import pl.cottageconnect.comment.service.dao.CommentDAO;
import pl.cottageconnect.cottage.service.CottageService;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.service.VillageService;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentDAO commentDAO;
    private final UserService userService;
    private final VillageService villageService;
    private final CottageService cottageService;

    public Page<Comment> getCommentsByEntityId(Long entityId, CommentableType type, Principal connectedUser, Pageable pageable) {
        validateEntity(entityId, type, connectedUser);

        return commentDAO.getCommentsByEntityId(entityId, pageable);
    }

    public Comment addCommentToEntity(Long entityId, CommentableType type, Comment comment, Principal connectedUser) {
        validateEntity(entityId, type, connectedUser);

        User user = userService.getConnectedUser(connectedUser);
        Comment newComment = createComment(entityId, comment, user, type);

        return commentDAO.addComment(newComment);
    }

    private void validateEntity(Long entityId, CommentableType type, Principal connectedUser) {
        if (CommentableType.VILLAGE.equals(type)) {
            villageService.getVillage(entityId, connectedUser);
        } else if (CommentableType.COTTAGE.equals(type)) {
            cottageService.getCottage(entityId, connectedUser);
        } else {
            throw new IllegalArgumentException("Unsupported CommentableType: " + type);
        }
    }

    private static Comment createComment(Long entityId, Comment comment, User user, CommentableType type) {
        return Comment.builder()
                .content(comment.getContent())
                .type(type)
                .commentableId(entityId)
                .rating(comment.getRating())
                .user(user)
                .build();
    }
}
