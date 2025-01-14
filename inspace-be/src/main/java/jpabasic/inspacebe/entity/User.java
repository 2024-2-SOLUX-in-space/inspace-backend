package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Space> spaces;

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    // 팔로잉/팔로워 정리 테이블
    @OneToMany(mappedBy = "followingId", fetch = FetchType.LAZY)
    private List<Follow> followings;

    @OneToMany(mappedBy = "followerId", fetch = FetchType.LAZY)
    private List<Follow> followers;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
