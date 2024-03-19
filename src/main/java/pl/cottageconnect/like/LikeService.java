package pl.cottageconnect.like;

import java.security.Principal;

public interface LikeService {
    Like getLikeById(Long likeId);

    Like addLike(Long likeableId, LikeableType type, Principal connectedUser);

    void deleteLike(Long likeId, Principal connectedUser);
}
