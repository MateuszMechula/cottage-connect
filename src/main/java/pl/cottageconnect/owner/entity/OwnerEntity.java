package pl.cottageconnect.owner.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.village.entity.VillageEntity;

import java.util.Set;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_owner")
@EqualsAndHashCode(of = "ownerId")
@ToString(of = {"ownerId", "firstname", "lastname", "phone", "userId"})
public class OwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_id")
    private Integer userId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<VillageEntity> village;
}
