<<<<<<< HEAD
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
=======
package jpabasic.inspacebe.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jpabasic.inspacebe.dto.SpaceDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int spaceId;

    private String sname;
    private int sthumb;
    private String surl; //공간 url //나중에 방문할 때 ? 배포 이후 진행.. 고민해보기

    private Boolean is_public;

    private Boolean is_primary;

    @CreationTimestamp
    @Column(updatable=false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    // user entity와 매핑 관계 추가


    //DTO -> Entity로 변환
    public static Space toEntity(SpaceDto dto){
        return Space.builder()
                .sname(dto.getSname())
                .sthumb(dto.getSthumb())
                .is_primary(dto.getIs_primary())
                .is_public(dto.getIs_public())
                .created_at(dto.getCreated_at())
                .build();
    }

}
>>>>>>> e799886927a0899c21fc59810cc494b5cdd71bf4
