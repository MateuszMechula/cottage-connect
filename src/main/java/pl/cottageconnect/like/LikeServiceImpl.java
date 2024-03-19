package pl.cottageconnect.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.comment.CommentService;
import pl.cottageconnect.common.exception.exceptions.DuplicateLikeException;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.security.User;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.village.VillagePostService;

import java.security.Principal;
import java.util.Optional;

import static pl.cottageconnect.like.LikeServiceImpl.ErrorMessages.*;

@Service
@RequiredArgsConstructor
class LikeServiceImpl implements LikeService {
    private final LikeDAO likeDAO;
    private final UserService userService;
    private final CommentService commentServiceImpl;
    private final VillagePostService villagePostService;

    @Override
    public Like getLikeById(Long likeId) {
        return likeDAO.getLikeById(likeId)
                .orElseThrow(() -> new NotFoundException(LIKE_NOT_FOUND.formatted(likeId)));
    }

    @Override
    @Transactional
    public Like addLike(Long likeableId, LikeableType type, Principal connectedUser) {
        validateLikeable(likeableId, type, connectedUser);
        User user = userService.getConnectedUser(connectedUser);
        isLikeExistsForLikeable(likeableId, type, user.userId());

        Like likeToSave = createLike(likeableId, type, user);

        return likeDAO.addLike(likeToSave);
    }

    @Override
    @Transactional
    public void deleteLike(Long likeId, Principal connectedUser) {
        Like like = getLikeById(likeId);
        User user = userService.getConnectedUser(connectedUser);

        if (user.userId().equals(like.user().userId())) {
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
            case COMMENT -> commentServiceImpl.getCommentById(likeableId, connectedUser);
            case VILLAGE_POST -> villagePostService.findAllVillagePosts(likeableId, connectedUser);
            default -> throw new IllegalArgumentException(UNSUPPORTED_LIKEABLE_TYPE.formatted(type));
        }
    }

    public static final class ErrorMessages {
        public static final String UNSUPPORTED_LIKEABLE_TYPE = "Unsupported Likeable Type: [%s]";
        public static final String LIKE_ALREADY_EXISTS = "Like already exists for likeableId: [%s], type: [%s], userId: [%s]";
        public static final String LIKE_NOT_FOUND = "Like with ID: [%s] not found or you dont have access";
    }
}
