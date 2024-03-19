package pl.cottageconnect.cottage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.common.exception.exceptions.UnauthorizedAccessException;
import pl.cottageconnect.customer.CustomerService;
import pl.cottageconnect.security.User;
import pl.cottageconnect.security.UserService;
import pl.cottageconnect.village.Village;
import pl.cottageconnect.village.VillageService;

import java.security.Principal;
import java.util.List;

import static pl.cottageconnect.cottage.CottageServiceImpl.ErrorMessages.COTTAGE_DELETE_ACCESS_DENIED;
import static pl.cottageconnect.cottage.CottageServiceImpl.ErrorMessages.COTTAGE_NOT_FOUND;

@Service
@RequiredArgsConstructor
class CottageServiceImpl implements CottageService {
    private final CottageDAO cottageDAO;
    private final VillageService villageService;
    private final UserService userService;
    private final CustomerService customerService;

    @Override
    public Cottage getCottageWithCheck(Long cottageId, Principal connectedUser) {

        Cottage cottage = cottageDAO.getCottage(cottageId)
                .orElseThrow(() -> new NotFoundException(COTTAGE_NOT_FOUND.formatted(cottageId)));

        checkAccessRights(cottage, connectedUser);

        return cottage;
    }

    @Override
    @Transactional
    public List<Cottage> findAllCottages(Long villageId, Principal connectedUser) {
        Village village = villageService.getVillage(villageId, connectedUser);
        return village.cottages().stream().toList();
    }

    @Override
    @Transactional
    public Cottage updateCottage(Long cottageId, Cottage toUpdate, Principal connectedUser) {

        Cottage existingCottage = getCottageWithCheck(cottageId, connectedUser);
        Cottage updatedCottage = updateCottage(toUpdate, existingCottage);

        return cottageDAO.addCottage(updatedCottage);
    }

    @Override
    @Transactional
    public void addCottage(Long villageId, Cottage cottage, Principal connectedUser) {
        Village village = villageService.getVillage(villageId, connectedUser);
        cottageDAO.addCottage(cottage.withVillage(village));
    }

    @Override
    @Transactional
    public void deleteCottage(Long cottageId, Principal connectedUser) {
        getCottageWithCheck(cottageId, connectedUser);

        cottageDAO.deleteCottage(cottageId);

    }

    private static <T> T getUpdatedValue(T newValue, T existingValue) {
        return newValue != null ? newValue : existingValue;

    }

    Cottage updateCottage(Cottage toUpdate, Cottage existingCottage) {
        return Cottage.builder()
                .cottageId(existingCottage.cottageId())
                .cottageNumber(getUpdatedValue(toUpdate.cottageNumber(), existingCottage.cottageNumber()))
                .cottageSize(getUpdatedValue(toUpdate.cottageSize(), existingCottage.cottageSize()))
                .price(getUpdatedValue(toUpdate.price(), existingCottage.price()))
                .description(getUpdatedValue(toUpdate.description(), existingCottage.description()))
                .village(existingCottage.village())
                .reservations(existingCottage.reservations())
                .build();
    }

    private void checkAccessRights(Cottage cottage, Principal connectedUser) {
        User connUser = userService.getConnectedUser(connectedUser);

        if (!isOwnerOrCustomer(cottage, connUser)) {
            throw new UnauthorizedAccessException(COTTAGE_DELETE_ACCESS_DENIED.formatted(connUser.email()));
        }
    }

    private boolean isOwnerOrCustomer(Cottage cottage, User connectedUser) {
        Integer userId = connectedUser.userId();
        Integer ownerUserId = cottage.village().owner().userId();

        return userId.equals(ownerUserId) || isCustomer(userId);
    }

    private boolean isCustomer(Integer userId) {
        return customerService.findCustomerByUserId(userId) != null;
    }

    static final class ErrorMessages {
        static final String COTTAGE_NOT_FOUND = "Cottage with ID: [%s] not found or you dont have access";
        static final String COTTAGE_DELETE_ACCESS_DENIED =
                "Access denied. User [%s] does not have permissions to delete or update this cottage.";
    }
}
