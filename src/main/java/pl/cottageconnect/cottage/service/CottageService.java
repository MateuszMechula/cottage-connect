package pl.cottageconnect.cottage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.common.exception.exceptions.UnauthorizedAccessException;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.service.dao.CottageDAO;
import pl.cottageconnect.security.service.UserService;
import pl.cottageconnect.village.domain.Village;
import pl.cottageconnect.village.service.VillageService;

import java.security.Principal;
import java.util.List;

import static pl.cottageconnect.cottage.service.CottageService.ErrorMessages.COTTAGE_DELETE_ACCESS_DENIED;
import static pl.cottageconnect.cottage.service.CottageService.ErrorMessages.COTTAGE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CottageService {
    private final CottageDAO cottageDAO;
    private final VillageService villageService;
    private final UserService userService;

    @Transactional
    public Cottage getCottage(Long cottageId, Principal connectedUser) {

        Cottage cottage = cottageDAO.getCottage(cottageId)
                .orElseThrow(() -> new NotFoundException(COTTAGE_NOT_FOUND.formatted(cottageId)));

        checkAccessRights(cottage, connectedUser);

        return cottage;
    }

    @Transactional
    public List<Cottage> findAllCottages(Long villageId, Principal connectedUser) {
        Village village = villageService.getVillage(villageId, connectedUser);
        return village.getCottages().stream().toList();
    }

    @Transactional
    public Cottage updateCottage(Long cottageId, Cottage toUpdate, Principal connectedUser) {

        Cottage existingCottage = getCottage(cottageId, connectedUser);
        Cottage updatedCottage = updateCottage(toUpdate, existingCottage);

        return cottageDAO.addCottage(updatedCottage);
    }

    @Transactional
    public void addCottage(Long villageId, Cottage cottage, Principal connectedUser) {
        Village village = villageService.getVillage(villageId, connectedUser);

        cottageDAO.addCottage(cottage.withVillage(village));
    }

    @Transactional
    public void deleteCottage(Long cottageId, Principal connectedUser) {
        getCottage(cottageId, connectedUser);

        cottageDAO.deleteCottage(cottageId);

    }

    private static <T> T getUpdatedValue(T newValue, T existingValue) {
        return newValue != null ? newValue : existingValue;

    }

    private static Cottage updateCottage(Cottage toUpdate, Cottage existingCottage) {
        return Cottage.builder()
                .cottageId(existingCottage.getCottageId())
                .cottageNumber(getUpdatedValue(toUpdate.getCottageNumber(), existingCottage.getCottageNumber()))
                .cottageSize(getUpdatedValue(toUpdate.getCottageSize(), existingCottage.getCottageSize()))
                .price(getUpdatedValue(toUpdate.getPrice(), existingCottage.getPrice()))
                .description(getUpdatedValue(toUpdate.getDescription(), existingCottage.getDescription()))
                .village(existingCottage.getVillage())
                .reservations(existingCottage.getReservations())
                .build();
    }

    private void checkAccessRights(Cottage cottage, Principal connectedUser) {
        Integer userId = cottage.getVillage().getOwner().getUserId();
        Integer expectedUserId = userService.getConnectedUser(connectedUser).getUserId();
        String username = connectedUser.getName();

        if (!expectedUserId.equals(userId)) {
            throw new UnauthorizedAccessException(COTTAGE_DELETE_ACCESS_DENIED.formatted(username));
        }
    }

    static final class ErrorMessages {
        static final String COTTAGE_NOT_FOUND = "Cottage with ID: [%s] not found or you dont have access";
        static final String COTTAGE_DELETE_ACCESS_DENIED =
                "Access denied. User [%s] does not have permissions to delete this cottage.";
    }
}
