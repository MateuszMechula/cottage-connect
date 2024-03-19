package pl.cottageconnect.customer;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
@AllArgsConstructor
public class CustomerRepositoryManager {
    private final CustomerJpaRepository customerJpaRepository;

    public void deleteAll() {
        customerJpaRepository.deleteAll();
    }
}
