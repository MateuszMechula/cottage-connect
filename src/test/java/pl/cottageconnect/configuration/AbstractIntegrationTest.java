package pl.cottageconnect.configuration;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.cottageconnect.CottageConnectApplication;
import pl.cottageconnect.customer.CustomerRepositoryManager;
import pl.cottageconnect.owner.OwnerRepositoryManager;
import pl.cottageconnect.security.UserRepositoryManager;

@ActiveProfiles("test")
@Import(PersistenceContainerTestConfiguration.class)
@SpringBootTest(
        classes = CottageConnectApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class AbstractIntegrationTest {

    @Autowired
    private UserRepositoryManager userRepositoryManager;
    @Autowired
    private OwnerRepositoryManager ownerRepositoryManager;
    @Autowired
    private CustomerRepositoryManager customerRepositoryManager;


    @AfterEach
    void afterEach() {
        customerRepositoryManager.deleteAll();
        ownerRepositoryManager.deleteAll();
        userRepositoryManager.deleteAll();
    }
}
