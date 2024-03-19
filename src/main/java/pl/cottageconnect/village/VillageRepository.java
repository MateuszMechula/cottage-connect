package pl.cottageconnect.village;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class VillageRepository implements VillageDAO {

    private final VillageJpaRepository villageJpaRepository;
    private final VillageEntityMapper villageEntityMapper;

    @Override
    public Optional<Village> getVillage(Long villageId) {
        return villageJpaRepository.findById(villageId)
                .map(villageEntityMapper::mapFromEntity);
    }

    @Override
    public List<Village> findVillagesByUserId(Long ownerId) {
        return villageJpaRepository.findVillageByOwnerId(ownerId).stream()
                .map(villageEntityMapper::mapFromEntity).toList();
    }

    @Override
    public Optional<Village> findVillageByVillageIdAndUserId(Long villageId, Integer userId) {
        return villageJpaRepository.findVillageByVillageIdAndUserId(villageId, userId)
                .map(villageEntityMapper::mapFromEntity);
    }

    @Override
    public Village saveVillage(Village village) {
        VillageEntity toSave = villageEntityMapper.mapToEntity(village);
        VillageEntity saved = villageJpaRepository.save(toSave);
        return villageEntityMapper.mapFromEntity(saved);
    }

    @Override
    public void deleteVillage(Long villageId) {
        villageJpaRepository.deleteById(villageId);
    }
}
