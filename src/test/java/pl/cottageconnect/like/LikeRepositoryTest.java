package pl.cottageconnect.like;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryLike.testLike;
import static pl.cottageconnect.util.TestDataFactoryLike.testLikeEntity;

@ExtendWith(MockitoExtension.class)
class LikeRepositoryTest {
    @Mock
    private LikeJpaRepository likeJpaRepository;
    @Mock
    private LikeEntityMapper likeEntityMapper;
    @InjectMocks
    private LikeRepository likeRepository;

    @Test
    void shouldGetLikeByIdSuccessfully() {
        //given
        Long likeId = 1L;
        LikeEntity likeEntity = testLikeEntity();
        Like expectedLike = testLike();

        when(likeJpaRepository.findById(likeId)).thenReturn(Optional.ofNullable(likeEntity));
        when(likeEntityMapper.mapFromEntity(likeEntity)).thenReturn(expectedLike);
        //when
        Optional<Like> likeById = likeRepository.getLikeById(likeId);
        //then
        assertTrue(likeById.isPresent());
        Like like = likeById.get();
        assertEquals(expectedLike.likeId(), like.likeId());
        assertEquals(expectedLike.likeableId(), like.likeableId());
        assertEquals(expectedLike.type(), like.type());
    }

    @Test
    void shouldFindLikeByLikeableAndUserSuccessfully() {
        //given
        Long likeableId = 1L;
        Integer userId = 1;
        LikeableType type = LikeableType.COMMENT;
        LikeEntity likeEntity = testLikeEntity();
        Like expectedLike = testLike();


        when(likeJpaRepository.findLikeByLikeableAndUser(likeableId, type, userId))
                .thenReturn(Optional.ofNullable(likeEntity));
        when(likeEntityMapper.mapFromEntity(likeEntity)).thenReturn(expectedLike);
        //when
        Optional<Like> likeByLikeableAndUser = likeRepository.findLikeByLikeableAndUser(likeableId, type, userId);
        //then
        assertTrue(likeByLikeableAndUser.isPresent());
        Like like = likeByLikeableAndUser.get();
        assertEquals(expectedLike.likeId(), like.likeId());
        assertEquals(expectedLike.type(), like.type());
        assertEquals(expectedLike.likeableId(), like.likeableId());
    }

    @Test
    void shouldAddLikeSuccessfully() {
        //given
        Like expectedLike = testLike();
        LikeEntity likeEntity = testLikeEntity();

        when(likeEntityMapper.mapToEntity(expectedLike)).thenReturn(likeEntity);
        when(likeJpaRepository.save(likeEntity)).thenReturn(likeEntity);
        when(likeEntityMapper.mapFromEntity(likeEntity)).thenReturn(expectedLike);
        //when
        Like like = likeRepository.addLike(expectedLike);
        //then
        assertEquals(like, expectedLike);
    }

    @Test
    void shouldDeleteLikeSuccessfully() {
        //given
        Long likeId = 1L;

        doNothing().when(likeJpaRepository).deleteById(likeId);
        //when
        likeRepository.deleteLike(likeId);
        //then
        verify(likeJpaRepository, times(1)).deleteById(likeId);
    }
}