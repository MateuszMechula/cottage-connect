package pl.cottageconnect.owner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cottageconnect.common.exception.exceptions.NotFoundException;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.service.dao.OwnerDAO;

import static pl.cottageconnect.owner.service.OwnerService.ErrorMessages.OWNER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerDAO ownerDAO;

    public Owner findOwnerByUserId(Integer userId) {
        return ownerDAO.findOwnerByUserId(userId)
                .orElseThrow(() -> new NotFoundException(OWNER_NOT_FOUND.formatted(userId)));
    }

    static final class ErrorMessages {
        static final String OWNER_NOT_FOUND = "Owner with user ID: [%s] not found or you dont have access";
    }
}
