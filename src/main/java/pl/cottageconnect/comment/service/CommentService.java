package pl.cottageconnect.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.enums.CommentableType;
import pl.cottageconnect.comment.service.dao.CommentDAO;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.cottage.service.CottageService;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.service.VillageService;

import java.security.Principal;

import static pl.cottageconnect.comment.service.CommentService.ErrorMessages.COMMENT_NOT_FOUND;
import static pl.cottageconnect.comment.service.CommentService.ErrorMessages.UNSUPPORTED_COMMENTABLE_TYPE;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentDAO commentDAO;
    private final UserService userService;
    private final VillageService villageService;
    private final CottageService cottageService;

    @Transactional
    public Comment getCommentById(Long commentId, Principal connectedUser) {
        Comment comment = commentDAO.findCommentById(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND.formatted(commentId)));

        validateUser(comment.getUser(), connectedUser, commentId);

        return comment;
    }

    @Transactional
    public Page<Comment> getCommentsByCommentableId(Long commentableId, CommentableType type, Principal connectedUser,
                                                    Pageable pageable) {
        validateCommentable(commentableId, type, connectedUser);

        return commentDAO.getCommentsByCommentableId(commentableId, pageable);
    }

    @Transactional
    public Comment addCommentToCommentable(Long commentableId, CommentableType type, Comment comment,
                                           Principal connectedUser) {

        validateCommentable(commentableId, type, connectedUser);
        User user = userService.getConnectedUser(connectedUser);
        Comment newComment = createComment(commentableId, comment, user, type);

        return commentDAO.addComment(newComment);
    }

    @Transactional
    public Comment updateComment(Long commentId, Comment commentToUpdate, Principal connectedUser) {

        Comment existingComment = getCommentById(commentId, connectedUser);
        Comment updatedComment = getUpdatedComment(commentId, commentToUpdate, existingComment);
        return commentDAO.addComment(updatedComment);
    }

    @Transactional
    public void deleteCommentById(Long commentId, Principal connectedUser) {
        getCommentById(commentId, connectedUser);

        commentDAO.deleteCommentById(commentId);
    }

    private Comment getUpdatedComment(Long commentId, Comment commentToUpdate, Comment existingComment) {
        return Comment.builder()
                .commentId(commentId)
                .content(commentToUpdate.getContent() != null ? commentToUpdate.getContent() : existingComment.getContent())
                .type(existingComment.getType())
                .commentableId(existingComment.getCommentableId())
                .rating(commentToUpdate.getRating() != null ? commentToUpdate.getRating() : existingComment.getRating())
                .user(existingComment.getUser())
                .build();
    }

    private void validateCommentable(Long entityId, CommentableType type, Principal connectedUser) {
        switch (type) {
            case VILLAGE -> villageService.getVillage(entityId, connectedUser);
            case COTTAGE -> cottageService.getCottage(entityId, connectedUser);
            default -> throw new IllegalArgumentException(UNSUPPORTED_COMMENTABLE_TYPE.formatted(type));
        }
    }

    private void validateUser(User commentUser, Principal connectedUser, Long commentId) {
        Integer expectedUserId = userService.getConnectedUser(connectedUser).getUserId();
        Integer userId = commentUser.getUserId();

        if (!expectedUserId.equals(userId)) {
            throw new NotFoundException(COMMENT_NOT_FOUND.formatted(commentId));
        }
    }

    private Comment createComment(Long entityId, Comment comment, User user, CommentableType type) {
        return Comment.builder()
                .content(comment.getContent())
                .type(type)
                .commentableId(entityId)
                .rating(comment.getRating())
                .user(user)
                .build();
    }

    static final class ErrorMessages {
        static final String COMMENT_NOT_FOUND = "Comment with ID: [%s] not found or you don't have access";
        static final String UNSUPPORTED_COMMENTABLE_TYPE = "Unsupported Commentable Type: [%s]";
    }
}
