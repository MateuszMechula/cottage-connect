package pl.cottageconnect.village.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.village.domain.VillagePost;
import pl.cottageconnect.village.entity.VillagePostEntity;
import pl.cottageconnect.village.repository.jpa.VillagePostJpaRepository;
import pl.cottageconnect.village.repository.mapper.VillagePostEntityMapper;
import pl.cottageconnect.village.service.dao.VillagePostDAO;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VillagePostRepository implements VillagePostDAO {
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
