package pl.cottageconnect.cottage.service.dao;

import pl.cottageconnect.cottage.domain.Cottage;

import java.util.Optional;

public interface CottageDAO {
    Optional<Cottage> getCottage(Long cottageId);

    Cottage addCottage(Cottage cottage);

    void deleteCottage(Long cottageId);
}
