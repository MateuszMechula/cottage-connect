package pl.cottageconnect.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {

    @Query("SELECT c FROM CustomerEntity c WHERE c.userId = :userId")
    Optional<CustomerEntity> findCustomerByUserId(Integer userId);
}
