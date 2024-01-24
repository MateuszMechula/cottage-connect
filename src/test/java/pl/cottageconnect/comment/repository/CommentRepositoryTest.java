package pl.cottageconnect.comment.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.cottageconnect.comment.domain.Comment;
import pl.cottageconnect.comment.entity.CommentEntity;
import pl.cottageconnect.comment.repository.jpa.CommentJpaRepository;
import pl.cottageconnect.comment.repository.mapper.CommentEntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryComment.*;

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
        Long commentableId = 1L;
        Pageable pageable = mock(Pageable.class);
        List<CommentEntity> comments = new ArrayList<>();
        comments.add(testCommentEntity());
        comments.add(testCommentEntity2());
        Page<CommentEntity> pageComments = new PageImpl<>(comments);

        when(commentJpaRepository.getCommentsByCommentableId(commentableId, pageable)).thenReturn(pageComments);
        when(commentEntityMapper.mapFromEntity(any(CommentEntity.class))).thenReturn(testComment3());
        //when
        Page<Comment> commentsByCommentableId = commentRepository.getCommentsByCommentableId(commentableId, pageable);
        //then
        assertEquals(2, commentsByCommentableId.getTotalElements());
        assertEquals(testComment3(), commentsByCommentableId.getContent().get(0));
        assertEquals(testComment4(), commentsByCommentableId.getContent().get(1));
    }

    @Test
    void shouldAddCommentSuccessfully() {
        //given
        Comment comment = testComment3();

        when(commentEntityMapper.mapToEntity(comment)).thenReturn(testCommentEntity());
        when(commentJpaRepository.save(any(CommentEntity.class))).thenReturn(testCommentEntity());
        when(commentEntityMapper.mapFromEntity(any(CommentEntity.class))).thenReturn(comment);
        //when
        Comment commentSaved = commentRepository.addComment(comment);
        //then
        verify(commentJpaRepository, times(1)).save(any(CommentEntity.class));
        assertEquals(comment, commentSaved);
    }

    @Test
    void shouldDeleteCommentByIdSuccessfully() {
        //given
        Long commentId = 1L;

        doNothing().when(commentJpaRepository).deleteById(commentId);
        //when
        commentRepository.deleteCommentById(commentId);
        //then
        verify(commentJpaRepository, times(1)).deleteById(commentId);
    }
}