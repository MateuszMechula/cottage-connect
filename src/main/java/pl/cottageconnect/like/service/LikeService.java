package pl.cottageconnect.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.comment.service.CommentService;
import pl.cottageconnect.common.exception.exceptions.DuplicateLikeException;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.like.domain.Like;
import pl.cottageconnect.like.enums.LikeableType;
import pl.cottageconnect.like.service.dao.LikeDAO;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.service.VillagePostService;

import java.security.Principal;
import java.util.Optional;

import static pl.cottageconnect.like.service.LikeService.ErrorMessages.*;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeDAO likeDAO;
    private final UserService userService;
    private final CommentService commentService;
    private final VillagePostService villagePostService;

    @Transactional
    public Like getLikeById(Long likeId) {
        return likeDAO.getLikeById(likeId)
                .orElseThrow(() -> new NotFoundException(LIKE_NOT_FOUND.formatted(likeId)));
    }

    @Transactional
    public Like addLike(Long likeableId, LikeableType type, Principal connectedUser) {
        validateLikeable(likeableId, type, connectedUser);
        User user = userService.getConnectedUser(connectedUser);
        isLikeExistsForLikeable(likeableId, type, user.getUserId());

        Like likeToSave = createLike(likeableId, type, user);

        return likeDAO.addLike(likeToSave);
    }

    @Transactional
    public void deleteLike(Long likeId, Principal connectedUser) {
        Like like = getLikeById(likeId);
        User user = userService.getConnectedUser(connectedUser);

        if (user.getUserId().equals(like.getUser().getUserId())) {
            likeDAO.deleteLike(likeId);
        }
    }

    private void isLikeExistsForLikeable(Long likeableId, LikeableType type, Integer userId) {
        Optional<Like> existingLike = likeDAO.findLikeByLikeableAndUser(likeableId, type, userId);
        if (existingLike.isPresent()) {
            throw new DuplicateLikeException(LIKE_ALREADY_EXISTS.formatted(likeableId, type, userId));
        }
    }

    private Like createLike(Long likeableId, LikeableType type, User user) {
        return Like.builder()
                .type(type)
                .likeableId(likeableId)
                .user(user)
                .build();
    }

    private void validateLikeable(Long likeableId, LikeableType type, Principal connectedUser) {
        switch (type) {
            case COMMENT -> commentService.getCommentById(likeableId, connectedUser);
            case VILLAGE_POST -> villagePostService.findAllVillagePosts(likeableId, connectedUser);
            default -> throw new IllegalArgumentException(UNSUPPORTED_LIKEABLE_TYPE.formatted(type));
        }
    }

    static final class ErrorMessages {
        static final String UNSUPPORTED_LIKEABLE_TYPE = "Unsupported Likeable Type: [%s]";
        static final String LIKE_ALREADY_EXISTS = "Like already exists for likeableId: [%s], type: [%s], userId: [%s]";
        static final String LIKE_NOT_FOUND = "Like with ID: [%s] not found or you dont have access";
    }
}
