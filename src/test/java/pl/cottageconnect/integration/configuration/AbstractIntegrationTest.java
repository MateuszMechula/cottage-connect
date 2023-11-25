package pl.cottageconnect.integration.configuration;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.cottageconnect.CottageConnectApplication;
import pl.cottageconnect.security.repository.jpa.UserJpaRepository;

@ActiveProfiles("test")
@Import(PersistenceContainerTestConfiguration.class)
@SpringBootTest(
        classes = CottageConnectApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class AbstractIntegrationTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @AfterEach
    void afterEach() {
        userJpaRepository.deleteAll();
    }
}
