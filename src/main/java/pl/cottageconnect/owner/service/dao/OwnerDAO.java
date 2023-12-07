package pl.cottageconnect.owner.service.dao;

import pl.cottageconnect.owner.domain.Owner;

public interface OwnerDAO {
    Owner save(Owner owner);

    Owner findOwnerByUserId(Integer userId);
}
