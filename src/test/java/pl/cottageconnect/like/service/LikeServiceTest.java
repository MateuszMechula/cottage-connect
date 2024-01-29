package pl.cottageconnect.like.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.service.CommentService;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.like.domain.Like;
import pl.cottageconnect.like.enums.LikeableType;
import pl.cottageconnect.like.service.dao.LikeDAO;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.domain.VillagePost;
import pl.cottageconnect.village.service.VillagePostService;

import java.security.Principal;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.like.service.LikeService.ErrorMessages.LIKE_NOT_FOUND;
import static pl.cottageconnect.util.TestDataFactoryLike.testLike;
import static pl.cottageconnect.util.TestDataFactoryUser.testUser;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    @Mock
    private LikeDAO likeDAO;
    @Mock
    private UserService userService;
    @Mock
    private CommentService commentService;
    @Mock
    private VillagePostService villagePostService;
    @InjectMocks
    private LikeService likeService;

    @Test
    void shouldGetLikeByIdSuccessfully() {
        //given
        Long likeId = 1L;
        Like like = testLike();
        when(likeDAO.getLikeById(likeId)).thenReturn(Optional.ofNullable(like));
        //when
        Like likeById = likeService.getLikeById(likeId);
        //then
        assertEquals(like, likeById);
    }

    @Test
    void shouldThrowExceptionWhenGetLikeById() {
        //given
        Long likeId = 1L;
        when(likeDAO.getLikeById(likeId)).thenThrow(new NotFoundException(LIKE_NOT_FOUND.formatted(likeId)));
        //when,then
        assertThrows(NotFoundException.class, () -> likeService.getLikeById(likeId));
    }

    @Test
    void shouldAddLikeWithTypeCommentSuccessfully() {
        //given
        Long likeableId = 1L;
        Integer userId = 1;
        LikeableType type = LikeableType.COMMENT;
        Principal connectedUser = mock(Principal.class);
        User user = testUser().withUserId(userId);
        Like expectedLike = testLike();

        when(commentService.getCommentById(likeableId, connectedUser)).thenReturn(Comment.builder().build());
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        when(likeDAO.findLikeByLikeableAndUser(likeableId, type, userId)).thenReturn(Optional.empty());
        when(likeDAO.addLike(any(Like.class))).thenReturn(expectedLike);
        //when
        Like like = likeService.addLike(likeableId, type, connectedUser);
        //then
        assertEquals(expectedLike.getLikeId(), like.getLikeId());
        assertEquals(expectedLike.getType(), like.getType());
        assertEquals(expectedLike.getLikeableId(), like.getLikeableId());
    }

    @Test
    void shouldAddLikeWithTypeVillagePostSuccessfully() {
        //given
        Long likeableId = 1L;
        Integer userId = 1;
        LikeableType type = LikeableType.VILLAGE_POST;
        Principal connectedUser = mock(Principal.class);
        User user = testUser().withUserId(userId);
        Like expectedLike = testLike();

        when(villagePostService.findAllVillagePosts(likeableId, connectedUser))
                .thenReturn(singletonList(VillagePost.builder().build()));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        when(likeDAO.findLikeByLikeableAndUser(likeableId, type, userId)).thenReturn(Optional.empty());
        when(likeDAO.addLike(any(Like.class))).thenReturn(expectedLike);
        //when
        Like like = likeService.addLike(likeableId, type, connectedUser);
        //then
        assertEquals(expectedLike.getLikeId(), like.getLikeId());
        assertEquals(expectedLike.getType(), like.getType());
        assertEquals(expectedLike.getLikeableId(), like.getLikeableId());
    }

    @Test
    void shouldThrowExceptionWhenAddLikeWithTypeOther() {
        //given
        Long likeableId = 1L;
        LikeableType type = LikeableType.OTHER;
        Principal connectedUser = mock(Principal.class);

        //when, then
        assertThrows(IllegalArgumentException.class, () -> likeService.addLike(likeableId, type, connectedUser));
    }

    @Test
    void shouldDeleteLikeSuccessfully() {
        //given
        Long likeId = 1L;
        User user = testUser().withUserId(1);
        Like like = testLike().withUser(user);
        Principal connectedUser = mock(Principal.class);

        when(likeDAO.getLikeById(likeId)).thenReturn(Optional.ofNullable(like));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        doNothing().when(likeDAO).deleteLike(likeId);
        //when
        likeService.deleteLike(likeId, connectedUser);
        //then
        verify(likeDAO, times(1)).deleteLike(likeId);
    }
}