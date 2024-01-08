package pl.cottageconnect.like.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.like.domain.Like;
import pl.cottageconnect.like.entity.LikeEntity;
import pl.cottageconnect.like.enums.LikeableType;
import pl.cottageconnect.like.repository.jpa.LikeJpaRepository;
import pl.cottageconnect.like.repository.mapper.LikeEntityMapper;
import pl.cottageconnect.like.service.dao.LikeDAO;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeRepository implements LikeDAO {
    private final LikeJpaRepository likeJpaRepository;
    private final LikeEntityMapper likeEntityMapper;

    @Override
    public Optional<Like> getLikeById(Long likeId) {
        return likeJpaRepository.findById(likeId)
                .map(likeEntityMapper::mapFromEntity);
    }

    @Override
    public Optional<Like> findLikeByLikeableAndUser(Long likeableId, LikeableType type, Integer userId) {
        return likeJpaRepository.findLikeByLikeableAndUser(likeableId, type, userId)
                .map(likeEntityMapper::mapFromEntity);
    }

    @Override
    public Like addLike(Like likeToSave) {
        LikeEntity toSave = likeEntityMapper.mapToEntity(likeToSave);
        LikeEntity savedEntity = likeJpaRepository.save(toSave);
        return likeEntityMapper.mapFromEntity(savedEntity);
    }

    @Override
    public void deleteLike(Long likeId) {
        likeJpaRepository.deleteById(likeId);
    }
}
