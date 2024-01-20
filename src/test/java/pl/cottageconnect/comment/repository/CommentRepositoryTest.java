package pl.cottageconnect.comment.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.entity.CommentEntity;
import pl.cottageconnect.comment.repository.jpa.CommentJpaRepository;
import pl.cottageconnect.comment.repository.mapper.CommentEntityMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryComment.testComment2;
import static pl.cottageconnect.util.TestDataFactoryComment.testCommentEntity;

@ExtendWith(MockitoExtension.class)
class CommentRepositoryTest {
    @Mock
    private CommentJpaRepository commentJpaRepository;
    @Mock
    private CommentEntityMapper commentEntityMapper;
    @InjectMocks
    private CommentRepository commentRepository;

    @Test
    void shouldFindCommentByIdSuccessfully() {
        //given
        Long commentId = 1L;
        Comment comment = testComment2();
        CommentEntity commentEntity = testCommentEntity();

        when(commentJpaRepository.findById(commentId)).thenReturn(Optional.ofNullable(commentEntity));
        when(commentEntityMapper.mapFromEntity(commentEntity)).thenReturn(comment);
        //when
        Optional<Comment> commentById = commentRepository.findCommentById(commentId);
        //then
        verify(commentJpaRepository, times(1)).findById(commentId);
        assertTrue(commentById.isPresent());
        Comment commentFound = commentById.get();
        assertAll(
                () -> assertEquals(comment.getCommentId(), commentFound.getCommentId()),
                () -> assertEquals(comment.getContent(), commentFound.getContent()),
                () -> assertEquals(comment.getRating(), commentFound.getRating()),
                () -> assertEquals(comment.getType(), commentFound.getType()),
                () -> assertEquals(comment.getCommentableId(), commentFound.getCommentableId())
        );
    }

    @Test
    void shouldGetCommentsByCommentableIdSuccessfully() {
        //given

        //when

        //then
    }

    @Test
    void shouldAddCommentSuccessfully() {
        //given

        //when

        //then
    }

    @Test
    void shouldDeleteCommentByIdSuccessfully() {
        //given

        //when

        //then
    }
}