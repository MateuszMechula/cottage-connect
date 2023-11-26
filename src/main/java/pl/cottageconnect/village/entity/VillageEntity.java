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
public class VillageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "village_id")
    private Long villageId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "village")
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private OwnerEntity owner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "village")
    private Set<CottageEntity> cottages;

    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL)
    private Set<VillagePost> posts;
}
