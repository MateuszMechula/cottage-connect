package pl.cottageconnect.village.service.dao;

import pl.cottageconnect.village.domain.Village;

import java.util.List;
import java.util.Optional;

public interface VillageDAO {

    Optional<Village> getVillage(Long villageId);

    List<Village> findVillagesByUserId(Long ownerId);

    Optional<Village> findVillageByVillageIdAndUserId(Long villageId, Integer userId);

    Village saveVillage(Village village);

    void deleteVillage(Long villageId);
}
