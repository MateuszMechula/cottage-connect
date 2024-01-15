package pl.cottageconnect.owner.service.dao;

import pl.cottageconnect.owner.domain.Owner;

import java.util.Optional;

public interface OwnerDAO {
    Optional<Owner> findOwnerByUserId(Integer userId);

    Owner save(Owner owner);
}
