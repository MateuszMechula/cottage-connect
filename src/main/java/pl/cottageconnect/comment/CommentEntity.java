package pl.cottageconnect.comment;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.security.UserEntity;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_comment")
@EqualsAndHashCode(of = "commentId")
@ToString(of = {"commentId", "content", "type", "commentableId", "rating"})
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content")
    private String content;

    @Column(name = "commentable_type")
    @Enumerated(EnumType.STRING)
    private CommentableType type;

    @Column(name = "commentable_id")
    private Long commentableId;

    @Column(name = "rating")
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
