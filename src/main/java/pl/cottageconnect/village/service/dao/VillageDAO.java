package pl.cottageconnect.village.service.dao;

import pl.cottageconnect.village.domain.Village;

import java.util.Optional;

public interface VillageDAO {

    Village saveVillage(Village village);

    Optional<Village> getVillage(Long villageId);

    void deleteVillage(Long villageId);

}
