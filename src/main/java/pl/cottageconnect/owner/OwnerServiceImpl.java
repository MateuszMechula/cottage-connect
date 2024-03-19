package pl.cottageconnect.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;

import static pl.cottageconnect.owner.OwnerServiceImpl.ErrorMessages.OWNER_NOT_FOUND;

@Service
@RequiredArgsConstructor
class OwnerServiceImpl implements OwnerService {
    private final OwnerDAO ownerDAO;

    @Override
    public Owner findOwnerByUserId(Integer userId) {
        return ownerDAO.findOwnerByUserId(userId)
                .orElseThrow(() -> new NotFoundException(OWNER_NOT_FOUND.formatted(userId)));
    }

    @Override
    public Owner save(Owner owner) {
        return ownerDAO.save(owner);
    }

    public static final class ErrorMessages {
        public static final String OWNER_NOT_FOUND = "Owner with user ID: [%s] not found or you dont have access";
    }
}
