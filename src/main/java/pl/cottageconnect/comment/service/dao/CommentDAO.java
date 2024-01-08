package pl.cottageconnect.comment.service.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.cottageconnect.comment.domain.Comment;

import java.util.Optional;

public interface CommentDAO {
    Optional<Comment> findCommentById(Long commentId);

    Page<Comment> getCommentsByCommentableId(Long villageId, Pageable pageable);

    Comment addComment(Comment newComment);

    void deleteCommentById(Long commentId);
}
