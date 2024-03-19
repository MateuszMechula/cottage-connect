package pl.cottageconnect.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class LikeRepository implements LikeDAO {
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
