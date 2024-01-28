package pl.cottageconnect.cottage.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cottageconnect.cottage.domain.Cottage;
import pl.cottageconnect.cottage.entity.CottageEntity;
import pl.cottageconnect.cottage.repository.jpa.CottageJpaRepository;
import pl.cottageconnect.cottage.repository.mapper.CottageEntityMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pl.cottageconnect.util.TestDataFactoryCottage.testCottage;
import static pl.cottageconnect.util.TestDataFactoryCottage.testCottageEntity;

@ExtendWith(MockitoExtension.class)
class CottageRepositoryTest {
    @Mock
    private CottageJpaRepository cottageJpaRepository;
    @Mock
    private CottageEntityMapper cottageEntityMapper;
    @InjectMocks
    private CottageRepository cottageRepository;

    @Test
    void shouldGetCottageSuccessfully() {
        //given
        Long cottageId = 1L;
        Cottage expectedCottage = testCottage();
        CottageEntity cottageEntity = testCottageEntity();

        when(cottageJpaRepository.findById(cottageId)).thenReturn(Optional.of(cottageEntity));
        when(cottageEntityMapper.mapFromEntity(cottageEntity)).thenReturn(expectedCottage);
        //when
        Optional<Cottage> cottage = cottageRepository.getCottage(cottageId);
        //then
        verify(cottageJpaRepository, times(1)).findById(cottageId);
        assertTrue(cottage.isPresent());
        Cottage cottageResponse = cottage.get();
        assertEquals(expectedCottage, cottageResponse);

    }

    @Test
    void shouldAddCottageSuccessfully() {
        //given
        Cottage expectedCottage = testCottage();
        CottageEntity cottageEntity = testCottageEntity();

        when(cottageEntityMapper.mapToEntity(expectedCottage)).thenReturn(cottageEntity);
        when(cottageJpaRepository.save(cottageEntity)).thenReturn(cottageEntity);
        when(cottageEntityMapper.mapFromEntity(cottageEntity)).thenReturn(expectedCottage);
        //when
        Cottage cottage = cottageRepository.addCottage(expectedCottage);
        //then
        verify(cottageJpaRepository, times(1)).save(cottageEntity);
        assertEquals(expectedCottage, cottage);
    }

    @Test
    void shouldDeleteCottageSuccessfully() {
        //given
        Long cottageId = 1L;
        //when
        cottageRepository.deleteCottage(cottageId);
        //then
        verify(cottageJpaRepository, times(1)).deleteById(cottageId);
    }
}