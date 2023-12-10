package pl.cottageconnect.village.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.village.entity.VillagePostEntity;

import java.util.List;

@Repository
public interface VillagePostJpaRepository extends JpaRepository<VillagePostEntity, Long> {

    @Query("SELECT vp FROM VillagePostEntity vp JOIN FETCH vp.village v WHERE v.villageId = :villageId")
    List<VillagePostEntity> findAllByVillageId(@Param("villageId") Long villageId);
}
