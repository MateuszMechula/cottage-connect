package pl.cottageconnect.owner.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.owner.domain.Owner;
import pl.cottageconnect.owner.entity.OwnerEntity;
import pl.cottageconnect.owner.repository.jpa.OwnerJpaRepository;
import pl.cottageconnect.owner.repository.mapper.OwnerEntityMapper;
import pl.cottageconnect.owner.service.dao.OwnerDAO;

import java.util.Optional;

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
    public Optional<Owner> findOwnerByUserId(Integer userId) {
        return ownerJpaRepository.findOwnerByUserId(userId)
                .map(ownerEntityMapper::mapFromEntity);

    }
}
