package pl.cottageconnect.like.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.like.enums.LikeableType;
import pl.cottageconnect.security.entity.UserEntity;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_like")
@EqualsAndHashCode(of = "likeId")
@ToString(of = {"likeId", "type", "likeableId"})
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @Column(name = "likeable_type")
    @Enumerated(EnumType.STRING)
    private LikeableType type;

    @Column(name = "likeable_id")
    private Long likeableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userId;

}
