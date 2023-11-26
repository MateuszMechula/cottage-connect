package pl.cottageconnect.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.cottageconnect.comment.enums.CommentableType;
import pl.cottageconnect.security.entity.UserEntity;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content")
    private String content;

    @Column(name = "commentable_type")
    private CommentableType type;

    @Column(name = "commentable_id")
    private Long commentableId;

    @Column(name = "rating")
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
