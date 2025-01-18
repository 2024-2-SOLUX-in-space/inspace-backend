package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following; // 팔로우하는 사람의 아이디

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed; // 팔로우 당하는 사람의 아이디

    public Follow(User followed, User following) {
        this.following = following;
        this.followed = followed;
    }
}
