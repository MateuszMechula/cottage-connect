package pl.cottageconnect.comment.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.comment.entity.CommentEntity;

@Repository
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT v FROM CommentEntity v where v.commentableId = :entityId")
    Page<CommentEntity> getCommentsByEntityId(@Param("entityId") Long entityId, Pageable pageable);
}
