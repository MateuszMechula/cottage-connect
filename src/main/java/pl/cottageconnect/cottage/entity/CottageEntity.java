package pl.cottageconnect.cottage.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.reservation.entity.ReservationEntity;
import pl.cottageconnect.village.entity.VillageEntity;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_cottage")
@EqualsAndHashCode(of = "cottageId")
@ToString(of = {"cottageId", "cottageNumber", "cottageSize", "price", "description"})
public class CottageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cottage_id")
    private Long cottageId;

    @Column(name = "cottage_number")
    private Integer cottageNumber;

    @Column(name = "cottage_size")
    private Integer cottageSize;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private VillageEntity village;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cottage", cascade = CascadeType.ALL)
    private Set<ReservationEntity> reservations;

}
