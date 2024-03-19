package pl.cottageconnect.owner;

import java.util.Optional;

interface OwnerDAO {
    Optional<Owner> findOwnerByUserId(Integer userId);

    Owner save(Owner owner);
}
