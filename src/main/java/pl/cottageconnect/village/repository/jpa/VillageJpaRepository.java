package pl.cottageconnect.village.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.cottageconnect.village.entity.VillageEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface VillageJpaRepository extends JpaRepository<VillageEntity, Long> {

    @Query("SELECT v FROM VillageEntity v JOIN FETCH v.owner o where o.ownerId = :ownerId")
    List<VillageEntity> findVillageByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT v FROM VillageEntity v WHERE v.villageId = :villageId AND v.owner.userId = :userId")
    Optional<VillageEntity> findVillageByVillageIdAndUserId(@Param("villageId") Long villageId,
                                                            @Param("userId") Integer userId);

}
