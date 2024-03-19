package pl.cottageconnect.cottage;

import java.util.Optional;

interface CottageDAO {
    Optional<Cottage> getCottage(Long cottageId);

    Cottage addCottage(Cottage cottage);

    void deleteCottage(Long cottageId);
}
