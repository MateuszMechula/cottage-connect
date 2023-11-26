package pl.cottageconnect.photo.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.photo.enums.PhotoableType;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_photo")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "photoable_type")
    private PhotoableType type;

    @Column(name = "photoable_id")
    private Long photoableId;
}
