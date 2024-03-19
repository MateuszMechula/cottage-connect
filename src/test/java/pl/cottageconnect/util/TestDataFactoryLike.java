package pl.cottageconnect.util;

import pl.cottageconnect.like.Like;
import pl.cottageconnect.like.LikeEntity;
import pl.cottageconnect.like.LikeableType;
import pl.cottageconnect.like.controller.dto.LikeDTO;

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
