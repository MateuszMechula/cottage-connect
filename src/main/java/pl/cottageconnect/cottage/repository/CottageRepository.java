package pl.cottageconnect.cottage.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.entity.CottageEntity;
import pl.cottageconnect.cottage.repository.jpa.CottageJpaRepository;
import pl.cottageconnect.cottage.repository.mapper.CottageEntityMapper;
import pl.cottageconnect.cottage.service.dao.CottageDAO;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CottageRepository implements CottageDAO {
    private final CottageJpaRepository cottageJpaRepository;
    private final CottageEntityMapper cottageEntityMapper;

    @Override
    public Optional<Cottage> getCottage(Long cottageId) {
        return cottageJpaRepository.findById(cottageId)
                .map(cottageEntityMapper::mapFromEntity);
    }

    @Override
    public Cottage addCottage(Cottage cottage) {
        CottageEntity toSave = cottageEntityMapper.mapToEntity(cottage);
        CottageEntity saved = cottageJpaRepository.save(toSave);
        return cottageEntityMapper.mapFromEntity(saved);
    }

    @Override
    public void deleteCottage(Long cottageId) {
        cottageJpaRepository.deleteById(cottageId);
    }
}
