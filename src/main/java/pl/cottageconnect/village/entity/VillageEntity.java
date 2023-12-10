package pl.cottageconnect.village.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.address.entity.AddressEntity;
import pl.cottageconnect.cottage.entity.CottageEntity;
import pl.cottageconnect.owner.entity.OwnerEntity;

import java.util.Set;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_village")
@EqualsAndHashCode(of = "villageId")
@ToString(of = {"villageId", "name", "description"})
public class VillageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "village_id")
    private Long villageId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private OwnerEntity owner;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "village")
    private Set<CottageEntity> cottages;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "village")
    private Set<VillagePostEntity> posts;
}
