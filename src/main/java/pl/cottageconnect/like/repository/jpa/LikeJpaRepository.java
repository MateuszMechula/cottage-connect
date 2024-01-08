package pl.cottageconnect.like.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.like.entity.LikeEntity;
import pl.cottageconnect.like.enums.LikeableType;

import java.util.Optional;

@Repository
public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {

    @Query("SELECT l FROM LikeEntity l WHERE l.likeableId = :likeableId AND l.type = :type AND l.user.userId = :userId")
    Optional<LikeEntity> findLikeByLikeableAndUser(
            @Param("likeableId") Long likeableId,
            @Param("type") LikeableType type,
            @Param("userId") Integer userId);
}
