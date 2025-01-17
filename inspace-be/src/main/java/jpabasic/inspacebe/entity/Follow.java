package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Data
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User following; // 팔로우하는 사람의 아이디

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User followed; // 팔로우 당하는 사람의 아이디

    public Follow(User following, User followed) {
        this.following = following;
        this.followed = followed;
    }
}
