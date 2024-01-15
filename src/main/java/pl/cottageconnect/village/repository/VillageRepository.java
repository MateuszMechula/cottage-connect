package pl.cottageconnect.village.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.entity.VillageEntity;
import pl.cottageconnect.village.repository.jpa.VillageJpaRepository;
import pl.cottageconnect.village.repository.mapper.VillageEntityMapper;
import pl.cottageconnect.village.service.dao.VillageDAO;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VillageRepository implements VillageDAO {

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
