package pl.cottageconnect.village.service.dao;

import pl.cottageconnect.village.domain.VillagePost;

import java.util.List;
import java.util.Optional;

public interface VillagePostDAO {

    Optional<VillagePost> findVillagePostById(Long villagePostId);

    List<VillagePost> findAllVillagePostsByVillageId(Long villageId);

    VillagePost saveVillagePost(VillagePost toSave);

    void deleteVillagePost(Long villageId);
}
