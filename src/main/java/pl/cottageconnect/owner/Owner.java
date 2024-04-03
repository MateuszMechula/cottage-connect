package pl.cottageconnect.owner;

import lombok.Builder;
import lombok.With;
import pl.cottageconnect.security.Person;
import pl.cottageconnect.village.Village;

import java.util.Set;

@With
@Builder
public record Owner(Long ownerId,
                    String firstname,
                    String lastname,
                    String phone,
                    Integer userId,
                    Set<Village> village) implements Person {
}
