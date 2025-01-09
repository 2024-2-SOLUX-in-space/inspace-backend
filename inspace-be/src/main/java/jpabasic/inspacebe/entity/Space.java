package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "space")
@Getter
@Setter
@NoArgsConstructor
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "space_id", nullable = false)
    private Integer spaceId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "sname", length = 10)
    private String sname;

    @Column(name = "sthumb")
    private Integer sthumb;

    @Column(name = "surl", columnDefinition = "TEXT")
    private String surl;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    public Space(User user, String sname, Boolean isPublic, Boolean isPrimary, LocalDateTime createdAt) {
        this.user = user;
        this.sname = sname;
        this.isPublic = isPublic;
        this.isPrimary = isPrimary;
        this.createdAt = createdAt;
    }
}
