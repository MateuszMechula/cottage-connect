package pl.cottageconnect.owner.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.entity.OwnerEntity;
import pl.cottageconnect.owner.repository.jpa.OwnerJpaRepository;
import pl.cottageconnect.owner.repository.mapper.OwnerEntityMapper;
import pl.cottageconnect.owner.service.dao.OwnerDAO;

@Repository
@RequiredArgsConstructor
public class OwnerRepository implements OwnerDAO {
    private final OwnerJpaRepository ownerJpaRepository;
    private final OwnerEntityMapper ownerEntityMapper;

    @Override
    public Owner save(Owner owner) {
        OwnerEntity toSave = ownerEntityMapper.mapToEntity(owner);
        OwnerEntity saved = ownerJpaRepository.save(toSave);
        return ownerEntityMapper.mapFromEntity(saved);
    }

    @Override
    public Owner findOwnerByUserId(Integer userId) {
        OwnerEntity owner = ownerJpaRepository.findOwnerEntityByUserId(userId);
        return ownerEntityMapper.mapFromEntity(owner);
    }
}
