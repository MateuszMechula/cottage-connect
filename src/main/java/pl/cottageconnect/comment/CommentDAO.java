package pl.cottageconnect.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

interface CommentDAO {
    Optional<Comment> findCommentById(Long commentId);

    Page<Comment> getCommentsByCommentableId(Long villageId, Pageable pageable);

    Comment addComment(Comment newComment);

    void deleteCommentById(Long commentId);
}
