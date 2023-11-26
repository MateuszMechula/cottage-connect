package pl.cottageconnect.customer.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.reservation.entity.ReservationEntity;

import java.util.Set;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "firsname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_id")
    private Integer userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<ReservationEntity> reservations;
}
