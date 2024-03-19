package pl.cottageconnect.village;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.security.UserEntity;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_village_post")
@EqualsAndHashCode(of = "villagePostId")
@ToString(of = {"villagePostId", "title", "content", "createdAt"})
public class VillagePostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "village_post_id")
    private Long villagePostId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "village_id")
    private VillageEntity village;

}
