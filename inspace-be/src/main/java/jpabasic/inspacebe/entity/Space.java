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
    private int space_id;

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
