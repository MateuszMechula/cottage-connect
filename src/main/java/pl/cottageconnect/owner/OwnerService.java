package pl.cottageconnect.owner;

public interface OwnerService {
    Owner save(Owner owner);

    Owner findOwnerByUserId(Integer userId);
}
