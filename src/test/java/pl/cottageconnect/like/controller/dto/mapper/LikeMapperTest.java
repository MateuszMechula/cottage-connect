package pl.cottageconnect.like.controller.dto.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.like.Like;
import pl.cottageconnect.like.controller.dto.LikeDTO;

import static org.junit.Assert.assertEquals;
import static pl.cottageconnect.util.TestDataFactoryLike.testLike;

class LikeMapperTest {
    private final LikeMapper likeMapper = Mappers.getMapper(LikeMapper.class);

    @Test
    void shouldMapLikeToLikeDTO() {
        //given
        Like like = testLike();
        //when
        LikeDTO likeDTO = likeMapper.mapToDTO(like);
        //then
        assertEquals(like.likeId(), likeDTO.likeId());
        assertEquals(like.type(), likeDTO.type());
        assertEquals(like.likeableId(), likeDTO.likeableId());
    }
}