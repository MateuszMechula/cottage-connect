package pl.cottageconnect.village;

import java.util.List;
import java.util.Optional;

interface VillagePostDAO {

    Optional<VillagePost> findVillagePostById(Long villagePostId);

    List<VillagePost> findAllVillagePostsByVillageId(Long villageId);

    VillagePost saveVillagePost(VillagePost toSave);

    void deleteVillagePost(Long villageId);
}
