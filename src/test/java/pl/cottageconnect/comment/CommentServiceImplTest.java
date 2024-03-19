package pl.cottageconnect.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.cottage.CottageService;
import pl.cottageconnect.security.User;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.village.VillageService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.comment.TestDataFactoryComment.testComment;
import static pl.cottageconnect.util.TestDataFactoryCottage.testCottage;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserWithId;
import static pl.cottageconnect.village.util.TestDataFactoryVillage.testVillage;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentDAO commentDAO;
    @Mock
    private UserService userService;
    @Mock
    private VillageService villageService;
    @Mock
    private CottageService cottageServiceImpl;
    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @Test
    void shouldGetCommentByIdSuccessfully() {
        //given
        Long commentId = 1L;
        Principal connectedUser = mock(Principal.class);
        Comment comment = testComment();
        User user = testUserWithId();
        Comment commentWithUser = Comment.builder()
                .content(comment.content())
                .type(comment.type())
                .rating(comment.rating())
                .user(user)
                .build();

        when(commentDAO.findCommentById(commentId)).thenReturn(Optional.ofNullable(commentWithUser));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        //when
        Comment commentById = commentServiceImpl.getCommentById(commentId, connectedUser);
        //then
        assertEquals(commentWithUser, commentById);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetCommentById() {
        //given
        Long commentId = 1L;
        Principal connectedUser = mock(Principal.class);
        when(commentDAO.findCommentById(commentId)).thenReturn(Optional.empty());
        //when,then
        assertThrows(NotFoundException.class, () -> commentServiceImpl.getCommentById(commentId, connectedUser));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserIdIsIncorrect() {
        //given
        Long commentId = 1L;
        Principal connectedUser = mock(Principal.class);
        Comment comment = testComment();
        User expectedUser = testUserWithId();
        User user = testUserWithId().withUserId(2);
        Comment commentWithUser = Comment.builder()
                .content(comment.content())
                .type(comment.type())
                .rating(comment.rating())
                .user(user)
                .build();

        when(commentDAO.findCommentById(commentId)).thenReturn(Optional.ofNullable(commentWithUser));
        when(userService.getConnectedUser(connectedUser)).thenReturn(expectedUser);
        //when,then
        assertThrows(NotFoundException.class, () -> commentServiceImpl.getCommentById(commentId, connectedUser));
    }

    @Test
    void shouldGetCommentsByCommentableIdCottageSuccessfully() {
        // given
        Long commentableId = 1L;
        CommentableType type = CommentableType.COTTAGE;
        Principal connectedUser = mock(Principal.class);
        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> comments = Arrays.asList(testComment(), testComment(), testComment());
        Page<Comment> expectedCommentPage = new PageImpl<>(comments, pageable, comments.size());

        when(commentDAO.getCommentsByCommentableId(commentableId, pageable)).thenReturn(expectedCommentPage);
        when(cottageServiceImpl.getCottageWithCheck(commentableId, connectedUser)).thenReturn(testCottage());
        // when
        Page<Comment> actualCommentPage = commentServiceImpl.getCommentsByCommentableId(commentableId, type, connectedUser, pageable);
        // then
        assertNotNull(actualCommentPage);
        assertEquals(expectedCommentPage.getTotalElements(), actualCommentPage.getTotalElements());
    }

    @Test
    void shouldGetCommentsByCommentableIdVillageSuccessfully() {
        // given
        Long commentableId = 1L;
        CommentableType type = CommentableType.VILLAGE;
        Principal connectedUser = mock(Principal.class);
        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> comments = Arrays.asList(testComment(), testComment(), testComment());
        Page<Comment> expectedCommentPage = new PageImpl<>(comments, pageable, comments.size());

        when(commentDAO.getCommentsByCommentableId(commentableId, pageable)).thenReturn(expectedCommentPage);
        when(villageService.getVillage(commentableId, connectedUser)).thenReturn(testVillage());
        // when
        Page<Comment> actualCommentPage = commentServiceImpl.getCommentsByCommentableId(commentableId, type, connectedUser, pageable);
        // then
        assertNotNull(actualCommentPage);
        assertEquals(expectedCommentPage.getTotalElements(), actualCommentPage.getTotalElements());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGetCommentsByCommentableId() {
        // given
        Long commentableId = 1L;
        CommentableType type = CommentableType.ANY;
        Principal connectedUser = mock(Principal.class);
        Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThrows(IllegalArgumentException.class, () ->
                commentServiceImpl.getCommentsByCommentableId(commentableId, type, connectedUser, pageable));
    }

    @Test
    void shouldAddCommentToCommentableSuccessfully() {
        //given
        Long commentableId = 1L;
        CommentableType type = CommentableType.VILLAGE;
        Principal connectedUser = mock(Principal.class);
        Comment comment = testComment();
        User user = testUserWithId();
        Comment commentWithUser = Comment.builder()
                .content(comment.content())
                .type(comment.type())
                .rating(comment.rating())
                .user(user)
                .build();

        when(commentDAO.addComment(any(Comment.class))).thenReturn(commentWithUser);
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        //when
        Comment addedComment = commentServiceImpl.addCommentToCommentable(commentableId, type, comment, connectedUser);
        //then
        assertEquals(commentWithUser, addedComment);
    }

    @Test
    void ShouldUpdateCommentSuccessfully() {
        // given
        Long commentId = 1L;
        Comment commentToUpdate = testComment();
        Principal connectedUser = mock(Principal.class);
        User user = testUserWithId();
        Comment commentWithUser = Comment.builder()
                .content(commentToUpdate.content())
                .type(commentToUpdate.type())
                .rating(commentToUpdate.rating())
                .user(user)
                .build();

        when(commentDAO.findCommentById(commentId)).thenReturn(Optional.ofNullable(commentWithUser));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        when(commentDAO.addComment(any(Comment.class))).thenReturn(commentWithUser);

        // when
        Comment updatedComment = commentServiceImpl.updateComment(commentId, commentToUpdate, connectedUser);
        // then
        assertEquals(commentWithUser, updatedComment);
    }

    @Test
    void ShouldDeleteCommentByIdSuccessfully() {
        // given
        Long commentId = 1L;
        Principal connectedUser = mock(Principal.class);
        Comment comment = testComment();
        User user = testUserWithId();
        Comment commentWithUser = Comment.builder()
                .content(comment.content())
                .type(comment.type())
                .rating(comment.rating())
                .user(user)
                .build();

        when(commentDAO.findCommentById(commentId)).thenReturn(Optional.ofNullable(commentWithUser));
        when(userService.getConnectedUser(connectedUser)).thenReturn(user);
        // when
        commentServiceImpl.deleteCommentById(commentId, connectedUser);
        // then
        verify(commentDAO, times(1)).deleteCommentById(commentId);
    }
}