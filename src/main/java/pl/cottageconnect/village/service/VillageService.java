package pl.cottageconnect.village.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.address.domain.Address;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.service.OwnerService;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.service.dao.VillageDAO;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static pl.cottageconnect.village.service.VillagePostService.ErrorMessages.VILLAGE_POST_NOT_FOUND;
import static pl.cottageconnect.village.service.VillageService.ErrorMessages.VILLAGE_NOT_FOUND;
import static pl.cottageconnect.village.service.VillageService.ErrorMessages.VILLAGE_WITH_USER_ID_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class VillageService {

    private final VillageDAO villageDAO;
    private final UserService userService;
    private final OwnerService ownerService;

    @Transactional
    public Village getVillage(Long villageId, Principal connectedUser) {
        log.info("Fetching Village by ID: {}", villageId);

        checkVillageId(villageId, connectedUser);

        User user = userService.getConnectedUser(connectedUser);
        Integer userId = user.getUserId();

        Optional<Village> first = villageDAO.getVillage(villageId)
                .stream()
                .filter(village -> village.getOwner().getUserId().equals(userId))
                .findFirst();

        if (first.isEmpty()) {
            throw new NotFoundException(VILLAGE_POST_NOT_FOUND.formatted(villageId));
        }
        return first.get();
    }

    @Transactional
    public List<Village> findAllVillage(Principal connectedUser) {

        User user = userService.getConnectedUser(connectedUser);
        Integer userId = user.getUserId();

        Owner owner = ownerService.findOwnerByUserId(userId);
        Long ownerId = owner.getOwnerId();
        return villageDAO.findVillagesByUserId(ownerId);
    }

    @Transactional
    public Village updateVillage(Long villageId, Village toUpdate, Principal principal) {

        log.info("Starts updating Village with ID: {}", villageId);
        Village existingVillage = getVillage(villageId, principal);
        Village updatedVillage = updateVillage(existingVillage, toUpdate);
        return villageDAO.saveVillage(updatedVillage);
    }

    @Transactional
    public Village addVillage(Village village, Principal connectedUser) {
        log.info("Starts saving Village");

        User user = userService.getConnectedUser(connectedUser);
        Owner owner = ownerService.findOwnerByUserId(user.getUserId());

        Village toSave = village.withOwner(owner);

        return villageDAO.saveVillage(toSave);
    }

    @Transactional
    public void deleteVillage(Long villageId, Principal connectedUser) {
        log.info("Deleting Village by ID: {}", villageId);
        Integer userId = userService.getConnectedUser(connectedUser).getUserId();
        villageDAO.findVillageByVillageIdAndUserId(villageId, userId)
                .orElseThrow(() -> new NotFoundException(VILLAGE_WITH_USER_ID_NOT_FOUND.formatted(villageId, userId)));

        villageDAO.deleteVillage(villageId);
    }

    @Transactional
    public void checkVillageId(Long villageId, Principal connectedUser) {
        List<Village> allVillage = findAllVillage(connectedUser);

        boolean villageFound = allVillage.stream()
                .anyMatch(village -> village.getVillageId().equals(villageId));

        if (!villageFound) {
            throw new NotFoundException(VILLAGE_NOT_FOUND.formatted(villageId));
        }
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

    static final class ErrorMessages {
        static final String VILLAGE_NOT_FOUND =
                "Village with ID: [%s] not found or user does not have access";
        static final String VILLAGE_WITH_USER_ID_NOT_FOUND =
                "Village with ID: [%s] and user ID: [%s] not found or you dont have access";
    }
}
