package pl.cottageconnect.village;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.address.Address;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.owner.Owner;
import pl.cottageconnect.owner.OwnerService;
import pl.cottageconnect.security.User;
import pl.cottageconnect.security.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static pl.cottageconnect.village.VillagePostServiceImpl.ErrorMessages.VILLAGE_POST_NOT_FOUND;
import static pl.cottageconnect.village.VillageServiceImpl.ErrorMessages.VILLAGE_NOT_FOUND;
import static pl.cottageconnect.village.VillageServiceImpl.ErrorMessages.VILLAGE_WITH_USER_ID_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
class VillageServiceImpl implements VillageService {

    private final VillageDAO villageDAO;
    private final UserService userService;
    private final OwnerService ownerService;

    @Override
    public Village getVillage(Long villageId, Principal connectedUser) {
        log.info("Fetching Village by ID: {}", villageId);

        checkVillageId(villageId, connectedUser);

        User user = userService.getConnectedUser(connectedUser);
        Integer userId = user.userId();

        Optional<Village> first = villageDAO.getVillage(villageId)
                .stream()
                .filter(village -> village.owner().userId().equals(userId))
                .findFirst();

        if (first.isEmpty()) {
            throw new NotFoundException(VILLAGE_POST_NOT_FOUND.formatted(villageId));
        }
        return first.get();
    }

    @Override
    public List<Village> findAllVillage(Principal connectedUser) {

        User user = userService.getConnectedUser(connectedUser);
        Integer userId = user.userId();

        Owner owner = ownerService.findOwnerByUserId(userId);
        Long ownerId = owner.ownerId();
        return villageDAO.findVillagesByUserId(ownerId);
    }

    @Override
    @Transactional
    public Village updateVillage(Long villageId, Village toUpdate, Principal principal) {

        log.info("Starts updating Village with ID: {}", villageId);
        Village existingVillage = getVillage(villageId, principal);
        Village updatedVillage = updateVillage(existingVillage, toUpdate);
        return villageDAO.saveVillage(updatedVillage);
    }

    @Override
    @Transactional
    public Village addVillage(Village village, Principal connectedUser) {
        log.info("Starts saving Village");

        User user = userService.getConnectedUser(connectedUser);
        Owner owner = ownerService.findOwnerByUserId(user.userId());

        Village toSave = village.withOwner(owner);

        return villageDAO.saveVillage(toSave);
    }

    @Override
    @Transactional
    public void deleteVillage(Long villageId, Principal connectedUser) {
        log.info("Deleting Village by ID: {}", villageId);
        Integer userId = userService.getConnectedUser(connectedUser).userId();
        villageDAO.findVillageByVillageIdAndUserId(villageId, userId)
                .orElseThrow(() -> new NotFoundException(VILLAGE_WITH_USER_ID_NOT_FOUND.formatted(villageId, userId)));

        villageDAO.deleteVillage(villageId);
    }

    public void checkVillageId(Long villageId, Principal connectedUser) {
        List<Village> allVillage = findAllVillage(connectedUser);

        boolean villageFound = allVillage.stream()
                .anyMatch(village -> village.villageId().equals(villageId));

        if (!villageFound) {
            throw new NotFoundException(VILLAGE_NOT_FOUND.formatted(villageId));
        }
    }

    private Village updateVillage(Village existingVillage, Village toUpdate) {
        return Village.builder()
                .villageId(existingVillage.villageId())
                .name(toUpdate.name() != null ? toUpdate.name() : existingVillage.name())
                .description(toUpdate.description() != null ? toUpdate.description()
                        : existingVillage.description())
                .address(buildVillageAddress(existingVillage.address(), toUpdate.address()))
                .owner(existingVillage.owner())
                .build();
    }

    private Address buildVillageAddress(Address existingVillage, Address toUpdate) {
        return Address.builder()
                .addressId(existingVillage.addressId())
                .street(toUpdate.street() != null ? toUpdate.street()
                        : existingVillage.street())
                .postalCode(toUpdate.postalCode() != null ? toUpdate.postalCode()
                        : existingVillage.postalCode())
                .city(toUpdate.city() != null ? toUpdate.city() : existingVillage.city())
                .voivodeship(toUpdate.voivodeship() != null ? toUpdate.voivodeship()
                        : existingVillage.voivodeship())
                .country(toUpdate.country() != null ? toUpdate.country() : existingVillage.country())
                .build();
    }

    static final class ErrorMessages {
        static final String VILLAGE_NOT_FOUND =
                "Village with ID: [%s] not found or user does not have access";
        static final String VILLAGE_WITH_USER_ID_NOT_FOUND =
                "Village with ID: [%s] and user ID: [%s] not found or you dont have access";
    }
}
