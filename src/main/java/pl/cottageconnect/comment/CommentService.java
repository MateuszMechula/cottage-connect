package pl.cottageconnect.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface CommentService {
    Comment getCommentById(Long commentId, Principal connectedUser);

    Page<Comment> getCommentsByCommentableId(Long commentableId, CommentableType type, Principal connectedUser,
                                             Pageable pageable);

    Comment addCommentToCommentable(Long commentableId, CommentableType type, Comment comment,
                                    Principal connectedUser);

    Comment updateComment(Long commentId, Comment commentToUpdate, Principal connectedUser);

    void deleteCommentById(Long commentId, Principal connectedUser);
}
