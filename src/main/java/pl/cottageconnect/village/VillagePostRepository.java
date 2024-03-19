package pl.cottageconnect.village;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class VillagePostRepository implements VillagePostDAO {
    private final VillagePostJpaRepository villagePostJpaRepository;
    private final VillagePostEntityMapper villagePostEntityMapper;

    @Override
    public Optional<VillagePost> findVillagePostById(Long villagePostId) {
        return villagePostJpaRepository.findById(villagePostId)
                .map(villagePostEntityMapper::mapFromEntity);
    }

    @Override
    public List<VillagePost> findAllVillagePostsByVillageId(Long villageId) {
        return villagePostJpaRepository.findAllByVillageId(villageId).stream()
                .map(villagePostEntityMapper::mapFromEntity).toList();
    }

    @Override
    public VillagePost saveVillagePost(VillagePost villagePost) {
        VillagePostEntity toSave = villagePostEntityMapper.mapToEntity(villagePost);
        VillagePostEntity saved = villagePostJpaRepository.save(toSave);
        return villagePostEntityMapper.mapFromEntity(saved);
    }

    @Override
    public void deleteVillagePost(Long villageId) {
        villagePostJpaRepository.deleteById(villageId);
    }
}
