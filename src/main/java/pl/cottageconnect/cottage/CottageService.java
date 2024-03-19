package pl.cottageconnect.cottage;

import java.security.Principal;
import java.util.List;

public interface CottageService {
    Cottage getCottageWithCheck(Long cottageId, Principal connectedUser);

    List<Cottage> findAllCottages(Long villageId, Principal connectedUser);

    Cottage updateCottage(Long cottageId, Cottage toUpdate, Principal connectedUser);

    void addCottage(Long villageId, Cottage cottage, Principal connectedUser);

    void deleteCottage(Long cottageId, Principal connectedUser);
}
