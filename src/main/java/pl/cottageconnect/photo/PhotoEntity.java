package pl.cottageconnect.photo;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_photo")
@EqualsAndHashCode(of = "photoId")
@ToString(of = {"photoId", "type", "photoableId"})
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "photoable_type")
    @Enumerated(EnumType.STRING)
    private PhotoableType type;

    @Column(name = "photoable_id")
    private Long photoableId;

    @Column(name = "path")
    private String path;
}
