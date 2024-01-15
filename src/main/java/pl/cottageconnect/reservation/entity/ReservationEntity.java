package pl.cottageconnect.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.cottage.entity.CottageEntity;
import pl.cottageconnect.customer.entity.CustomerEntity;

import java.time.OffsetDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_reservation")
@EqualsAndHashCode(of = "reservationId")
@ToString(of = {"reservationId", "dayIn", "dayOut", "status"})
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "day_in")
    private OffsetDateTime dayIn;

    @Column(name = "day_out")
    private OffsetDateTime dayOut;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cottage_id")
    private CottageEntity cottage;
}
