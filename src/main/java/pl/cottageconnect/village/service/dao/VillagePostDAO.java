package pl.cottageconnect.village.service.dao;

import pl.cottageconnect.village.domain.VillagePost;

import java.util.List;
import java.util.Optional;

public interface VillagePostDAO {

    VillagePost saveVillagePost(VillagePost toSave);

    void deleteVillagePost(Long villageId);

    List<VillagePost> findAllVillagePostsByVillageId(Long villageId);

    Optional<VillagePost> findVillagePostById(Long villagePostId);
}
