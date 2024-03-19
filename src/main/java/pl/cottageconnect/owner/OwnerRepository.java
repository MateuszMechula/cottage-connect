package pl.cottageconnect.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class OwnerRepository implements OwnerDAO {
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
