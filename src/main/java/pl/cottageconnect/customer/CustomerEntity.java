package pl.cottageconnect.customer;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.reservation.ReservationEntity;

import java.util.Set;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_customer")
@EqualsAndHashCode(of = "customerId")
@ToString(of = {"customerId", "firstname", "lastname", "phone", "userId"})
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_id")
    private Integer userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<ReservationEntity> reservations;
}
