package pl.cottageconnect.village;

import java.security.Principal;
import java.util.List;

public interface VillagePostService {
    VillagePost findVillagePostById(Long villagePostId, Principal connectedUser);

    List<VillagePost> findAllVillagePosts(Long villageId, Principal connectedUser);

    VillagePost updateVillagePost(Long villagePostId, VillagePost toUpdate, Principal connectedUser);

    VillagePost addVillagePost(Long villageId, Principal connectedUser, VillagePost villagePost);

    void deleteVillagePost(Long villagePostId, Principal connectedUser);
}
