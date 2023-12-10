package pl.cottageconnect.owner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.service.dao.OwnerDAO;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerDAO ownerDAO;

    public Owner findOwnerByUserId(Integer userId) {
        return ownerDAO.findOwnerByUserId(userId);
    }
}
