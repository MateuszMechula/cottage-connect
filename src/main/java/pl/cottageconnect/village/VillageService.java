package pl.cottageconnect.village;

import java.security.Principal;
import java.util.List;

public interface VillageService {
    Village getVillage(Long villageId, Principal connectedUser);

    List<Village> findAllVillage(Principal connectedUser);

    void checkVillageId(Long villageId, Principal connectedUser);

    Village updateVillage(Long villageId, Village toUpdate, Principal principal);

    Village addVillage(Village village, Principal connectedUser);

    void deleteVillage(Long villageId, Principal connectedUser);

}
