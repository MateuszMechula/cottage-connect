package pl.cottageconnect.comment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.entity.CommentEntity;
import pl.cottageconnect.comment.repository.jpa.CommentJpaRepository;
import pl.cottageconnect.comment.repository.mapper.CommentEntityMapper;
import pl.cottageconnect.comment.service.dao.CommentDAO;

@Repository
@RequiredArgsConstructor
public class CommentRepository implements CommentDAO {
    private final CommentJpaRepository commentJpaRepository;
    private final CommentEntityMapper commentEntityMapper;

    @Override
    public Page<Comment> getCommentsByEntityId(Long entityId, Pageable pageable) {
        return commentJpaRepository.getCommentsByEntityId(entityId, pageable)
                .map(commentEntityMapper::mapFromEntity);
    }

    @Override
    public Comment addComment(Comment newComment) {
        CommentEntity toSave = commentEntityMapper.mapToEntity(newComment);
        CommentEntity savedEntity = commentJpaRepository.save(toSave);
        return commentEntityMapper.mapFromEntity(savedEntity);
    }
}
