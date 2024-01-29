package pl.cottageconnect.like.controller.dto.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.cottageconnect.like.controller.dto.LikeDTO;
import pl.cottageconnect.like.domain.Like;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertEquals(like.getLikeId(), likeDTO.likeId());
        assertEquals(like.getType(), likeDTO.type());
        assertEquals(like.getLikeableId(), likeDTO.likeableId());
    }
}