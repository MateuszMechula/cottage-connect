package pl.cottageconnect.owner;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
@AllArgsConstructor
public class OwnerRepositoryManager {
    private final OwnerJpaRepository ownerJpaRepository;

    public void deleteAll() {
        ownerJpaRepository.deleteAll();
    }
}
