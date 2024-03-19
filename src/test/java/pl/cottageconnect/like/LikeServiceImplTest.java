package pl.cottageconnect.like;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.comment.Comment;
import pl.cottageconnect.comment.CommentService;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.security.User;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.village.VillagePost;
import pl.cottageconnect.village.VillagePostService;

import java.security.Principal;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.like.LikeServiceImpl.ErrorMessages.LIKE_NOT_FOUND;
import static pl.cottageconnect.util.TestDataFactoryLike.testLike;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserRoleCustomer;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {
    @Mock
    private LikeDAO likeDAO;
    @Mock
    private UserService userService;
    @Mock
    private CommentService commentServiceImpl;
    @Mock
    private VillagePostService villagePostService;
    @InjectMocks
    private LikeServiceImpl likeService;

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
        User user = testUserRoleCustomer().withUserId(userId);
        Like expectedLike = testLike();

        when(commentServiceImpl.getCommentById(likeableId, connectedUser)).thenReturn(Comment.builder().build());
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        when(likeDAO.findLikeByLikeableAndUser(likeableId, type, userId)).thenReturn(Optional.empty());
        when(likeDAO.addLike(any(Like.class))).thenReturn(expectedLike);
        //when
        Like like = likeService.addLike(likeableId, type, connectedUser);
        //then
        assertEquals(expectedLike.likeId(), like.likeId());
        assertEquals(expectedLike.type(), like.type());
        assertEquals(expectedLike.likeableId(), like.likeableId());
    }

    @Test
    void shouldAddLikeWithTypeVillagePostSuccessfully() {
        //given
        Long likeableId = 1L;
        Integer userId = 1;
        LikeableType type = LikeableType.VILLAGE_POST;
        Principal connectedUser = mock(Principal.class);
        User user = testUserRoleCustomer().withUserId(userId);
        Like expectedLike = testLike();

        when(villagePostService.findAllVillagePosts(likeableId, connectedUser))
                .thenReturn(singletonList(VillagePost.builder().build()));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        when(likeDAO.findLikeByLikeableAndUser(likeableId, type, userId)).thenReturn(Optional.empty());
        when(likeDAO.addLike(any(Like.class))).thenReturn(expectedLike);
        //when
        Like like = likeService.addLike(likeableId, type, connectedUser);
        //then
        assertEquals(expectedLike.likeId(), like.likeId());
        assertEquals(expectedLike.type(), like.type());
        assertEquals(expectedLike.likeableId(), like.likeableId());
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
        User user = testUserRoleCustomer().withUserId(1);
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