package pl.cottageconnect.village.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.cottageconnect.address.domain.Address;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.service.OwnerService;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.exception.MissingUserException;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.exception.VillageNotFoundException;
import pl.cottageconnect.village.service.dao.VillageDAO;

import java.security.Principal;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class VillageService {

    private final VillageDAO villageDAO;
    private final UserService userService;
    private final OwnerService ownerService;

    public Village getVillage(Long villageId) {
        log.info("Fetching Village by ID: {}", villageId);
        assert villageId != null : "Village ID cannot be null";
        return villageDAO.getVillage(villageId)
                .orElseThrow(() ->
                        new VillageNotFoundException("Village with id: [%s] doesn't exists".formatted(villageId)));
    }

    @Transactional
    public Village updateVillage(Long villageId, Village toUpdate) {
        log.info("Starts updating Village with ID: {}", villageId);
        Village existingVillage = getVillage(villageId);
        Village updatedVillage = updateVillage(existingVillage, toUpdate);
        return villageDAO.saveVillage(updatedVillage);
    }

    @Transactional
    public Village addVillage(Village village, Principal connectedUser) {
        log.info("Starts saving Village");
        if (connectedUser == null) {
            throw new MissingUserException("Connected user is null");
        }
        String email = Objects.requireNonNull(connectedUser.getName());
        User existingUser = userService.getUserByUsername(email);

        Owner owner = ownerService.findOwnerByUserId(existingUser.getUserId());
        Village toSave = village.withOwner(owner);

        return villageDAO.saveVillage(toSave);
    }

    @Transactional
    public void deleteVillage(Long villageId) {
        log.info("Deleting Village by ID: {}", villageId);
        villageDAO.deleteVillage(villageId);
    }

    private Village updateVillage(Village existingVillage, Village toUpdate) {
        return Village.builder()
                .villageId(existingVillage.getVillageId())
                .name(toUpdate.getName() != null ? toUpdate.getName() : existingVillage.getName())
                .description(toUpdate.getDescription() != null ? toUpdate.getDescription()
                        : existingVillage.getDescription())
                .address(buildVillageAddress(existingVillage.getAddress(), toUpdate.getAddress()))
                .owner(existingVillage.getOwner())
                .build();
    }

    private Address buildVillageAddress(Address existingVillage, Address toUpdate) {
        return Address.builder()
                .addressId(existingVillage.getAddressId())
                .street(toUpdate.getStreet() != null ? toUpdate.getStreet()
                        : existingVillage.getStreet())
                .postalCode(toUpdate.getPostalCode() != null ? toUpdate.getPostalCode()
                        : existingVillage.getPostalCode())
                .city(toUpdate.getCity() != null ? toUpdate.getCity() : existingVillage.getCity())
                .voivodeship(toUpdate.getVoivodeship() != null ? toUpdate.getVoivodeship()
                        : existingVillage.getVoivodeship())
                .country(toUpdate.getCountry() != null ? toUpdate.getCountry() : existingVillage.getCountry())
                .build();
    }
}
