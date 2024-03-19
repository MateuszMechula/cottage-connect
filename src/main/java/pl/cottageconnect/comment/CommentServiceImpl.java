package pl.cottageconnect.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.cottage.CottageService;
import pl.cottageconnect.security.User;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.village.VillageService;

import java.security.Principal;

import static pl.cottageconnect.comment.CommentServiceImpl.ErrorMessages.COMMENT_NOT_FOUND;
import static pl.cottageconnect.comment.CommentServiceImpl.ErrorMessages.UNSUPPORTED_COMMENTABLE_TYPE;

@Service
@RequiredArgsConstructor
class CommentServiceImpl implements CommentService {
    private final CommentDAO commentDAO;
    private final UserService userService;
    private final VillageService villageService;
    private final CottageService cottageServiceImpl;

    @Override
    public Comment getCommentById(Long commentId, Principal connectedUser) {
        Comment comment = commentDAO.findCommentById(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND.formatted(commentId)));

        validateUser(comment.user(), connectedUser, commentId);

        return comment;
    }

    @Override
    public Page<Comment> getCommentsByCommentableId(Long commentableId, CommentableType type, Principal connectedUser,
                                                    Pageable pageable) {
        validateCommentable(commentableId, type, connectedUser);

        return commentDAO.getCommentsByCommentableId(commentableId, pageable);
    }

    @Override
    @Transactional
    public Comment addCommentToCommentable(Long commentableId, CommentableType type, Comment comment,
                                           Principal connectedUser) {

        validateCommentable(commentableId, type, connectedUser);
        User user = userService.getConnectedUser(connectedUser);
        Comment newComment = createComment(commentableId, comment, user, type);

        return commentDAO.addComment(newComment);
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, Comment commentToUpdate, Principal connectedUser) {

        Comment existingComment = getCommentById(commentId, connectedUser);
        Comment updatedComment = getUpdatedComment(commentId, commentToUpdate, existingComment);
        return commentDAO.addComment(updatedComment);
    }

    @Override
    @Transactional
    public void deleteCommentById(Long commentId, Principal connectedUser) {
        getCommentById(commentId, connectedUser);

        commentDAO.deleteCommentById(commentId);
    }

    private Comment getUpdatedComment(Long commentId, Comment commentToUpdate, Comment existingComment) {
        return Comment.builder()
                .commentId(commentId)
                .content(commentToUpdate.content() != null ? commentToUpdate.content() : existingComment.content())
                .type(existingComment.type())
                .commentableId(existingComment.commentableId())
                .rating(commentToUpdate.rating() != null ? commentToUpdate.rating() : existingComment.rating())
                .user(existingComment.user())
                .build();
    }

    private void validateCommentable(Long entityId, CommentableType type, Principal connectedUser) {
        switch (type) {
            case VILLAGE -> villageService.getVillage(entityId, connectedUser);
            case COTTAGE -> cottageServiceImpl.getCottageWithCheck(entityId, connectedUser);
            default -> throw new IllegalArgumentException(UNSUPPORTED_COMMENTABLE_TYPE.formatted(type));
        }
    }

    private void validateUser(User commentUser, Principal connectedUser, Long commentId) {
        Integer expectedUserId = userService.getConnectedUser(connectedUser).userId();
        Integer userId = commentUser.userId();

        if (!expectedUserId.equals(userId)) {
            throw new NotFoundException(COMMENT_NOT_FOUND.formatted(commentId));
        }
    }

    private Comment createComment(Long entityId, Comment comment, User user, CommentableType type) {
        return Comment.builder()
                .content(comment.content())
                .type(type)
                .commentableId(entityId)
                .rating(comment.rating())
                .user(user)
                .build();
    }

    static final class ErrorMessages {
        static final String COMMENT_NOT_FOUND = "Comment with ID: [%s] not found or you don't have access";
        static final String UNSUPPORTED_COMMENTABLE_TYPE = "Unsupported Commentable Type: [%s]";
    }
}
