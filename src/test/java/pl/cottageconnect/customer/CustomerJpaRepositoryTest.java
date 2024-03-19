package pl.cottageconnect.customer;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.cottageconnect.configuration.AbstractJpa;
import pl.cottageconnect.security.UserEntity;
import pl.cottageconnect.security.UserRepositoryManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cottageconnect.util.TestDataFactoryCustomer.testCustomerEntity;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource(properties = "test.name=CustomerJpaRepositoryTest")
@Import(UserRepositoryManager.class)
class CustomerJpaRepositoryTest extends AbstractJpa {

    private final CustomerJpaRepository customerJpaRepository;
    private final UserRepositoryManager userRepositoryManager;

    @Test
    void shouldFindCustomerByUserIdSuccessfully() {
        //given
        Integer userId = 1;
        UserEntity userEntity = testUserEntity();
        CustomerEntity customerEntity = testCustomerEntity();
        userEntity.setUserId(userId);
        customerEntity.setUserId(userId);

        userRepositoryManager.saveAndFlush(userEntity);
        customerJpaRepository.saveAndFlush(customerEntity);
        //when
        Optional<CustomerEntity> customerByUserId = customerJpaRepository.findCustomerByUserId(userId);
        //then
        assertTrue(customerByUserId.isPresent());
        assertEquals(customerEntity, customerByUserId.get());
    }
}