package pl.cottageconnect.like;

import java.util.Optional;

interface LikeDAO {

    Optional<Like> getLikeById(Long likeId);

    Optional<Like> findLikeByLikeableAndUser(Long likeableId, LikeableType type, Integer userId);

    Like addLike(Like likeToSave);

    void deleteLike(Long likeId);
}
