package pl.cottageconnect.comment.service.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.cottageconnect.comment.domain.Comment;

public interface CommentDAO {
    Comment addComment(Comment newComment);

    Page<Comment> getCommentsByEntityId(Long villageId, Pageable pageable);
}
