package pl.cottageconnect.customer.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.cottageconnect.configuration.AbstractJpa;
import pl.cottageconnect.customer.entity.CustomerEntity;
import pl.cottageconnect.security.entity.UserEntity;
import pl.cottageconnect.security.repository.jpa.UserJpaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cottageconnect.util.TestDataFactoryCustomer.testCustomerEntity;
import static pl.cottageconnect.util.TestDataFactoryUser.testUserEntity;

@AllArgsConstructor(onConstructor = @__(@Autowired))
class CustomerJpaRepositoryTest extends AbstractJpa {

    private final CustomerJpaRepository customerJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Test
    void shouldFindCustomerByUserIdSuccessfully() {
        //given
        Integer userId = 1;
        UserEntity userEntity = testUserEntity();
        CustomerEntity customerEntity = testCustomerEntity();
        userEntity.setUserId(userId);
        customerEntity.setUserId(userId);

        userJpaRepository.saveAndFlush(userEntity);
        customerJpaRepository.saveAndFlush(customerEntity);
        //when
        Optional<CustomerEntity> customerByUserId = customerJpaRepository.findCustomerByUserId(userId);
        //then
        assertTrue(customerByUserId.isPresent());
        assertEquals(customerEntity, customerByUserId.get());
    }
}