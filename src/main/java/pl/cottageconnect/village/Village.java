package pl.cottageconnect.village;

import lombok.Builder;
import lombok.With;
import pl.cottageconnect.address.Address;
import pl.cottageconnect.cottage.Cottage;
import pl.cottageconnect.owner.Owner;

import java.util.Set;

@With
@Builder
public record Village(Long villageId,
                      String name,
                      String description,
                      Owner owner,
                      Address address,
                      Set<Cottage> cottages,
                      Set<VillagePost> posts) {
}
