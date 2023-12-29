package pl.cottageconnect.address.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.village.entity.VillageEntity;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_address")
@EqualsAndHashCode(of = "addressId")
@ToString(of = {"addressId", "street", "postalCode", "city", "voivodeship", "country"})
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "street")
    private String street;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "voivodeship")
    private String voivodeship;

    @Column(name = "country")
    private String country;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private VillageEntity village;
}
