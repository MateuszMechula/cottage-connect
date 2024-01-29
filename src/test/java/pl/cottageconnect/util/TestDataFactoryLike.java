package pl.cottageconnect.util;

import pl.cottageconnect.like.controller.dto.LikeDTO;
import pl.cottageconnect.like.domain.Like;
import pl.cottageconnect.like.entity.LikeEntity;
import pl.cottageconnect.like.enums.LikeableType;

public class TestDataFactoryLike {

    public static Like testLike() {
        return Like.builder()
                .likeId(1L)
                .type(LikeableType.COMMENT)
                .likeableId(1L)
                .build();
    }

    public static LikeEntity testLikeEntity() {
        return LikeEntity.builder()
                .likeId(1L)
                .type(LikeableType.COMMENT)
                .likeableId(1L)
                .build();
    }

    public static LikeDTO testLikeDTO() {
        return LikeDTO.builder()
                .likeId(1L)
                .type(LikeableType.COMMENT)
                .likeableId(1L)
                .build();
    }
}
