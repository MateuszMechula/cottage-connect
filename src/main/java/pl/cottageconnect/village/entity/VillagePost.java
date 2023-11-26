package pl.cottageconnect.village.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.security.entity.UserEntity;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_village_post")
public class VillagePost {
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private VillageEntity village;

}
