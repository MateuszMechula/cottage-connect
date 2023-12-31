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

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository implements CommentDAO {
    private final CommentJpaRepository commentJpaRepository;
    private final CommentEntityMapper commentEntityMapper;

    @Override
    public Optional<Comment> findCommentById(Long commentId) {
        return commentJpaRepository.findById(commentId)
                .map(commentEntityMapper::mapFromEntity);
    }

    @Override
    public Page<Comment> getCommentsByCommentableId(Long commentableId, Pageable pageable) {
        return commentJpaRepository.getCommentsByCommentableId(commentableId, pageable)
                .map(commentEntityMapper::mapFromEntity);
    }

    @Override
    public Comment addComment(Comment newComment) {
        CommentEntity toSave = commentEntityMapper.mapToEntity(newComment);
        CommentEntity savedEntity = commentJpaRepository.save(toSave);
        return commentEntityMapper.mapFromEntity(savedEntity);
    }

    @Override
    public void deleteCommentById(Long commentId) {
        commentJpaRepository.deleteById(commentId);
    }
}
