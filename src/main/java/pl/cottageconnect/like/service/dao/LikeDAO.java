package pl.cottageconnect.like.service.dao;

import pl.cottageconnect.like.domain.Like;
import pl.cottageconnect.like.enums.LikeableType;

import java.util.Optional;

public interface LikeDAO {

    Optional<Like> getLikeById(Long likeId);

    Optional<Like> findLikeByLikeableAndUser(Long likeableId, LikeableType type, Integer userId);

    Like addLike(Like likeToSave);

    void deleteLike(Long likeId);
}
