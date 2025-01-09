package jpabasic.inspacebe.dto;

import jpabasic.inspacebe.entity.Space;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceDto {
    //user_id 추가해야함.
    private String sname;
    private int sthumb;
    private Boolean is_primary=false;
    private Boolean is_public=false;

    @CurrentTimestamp
    private LocalDateTime created_at;

    //Entity -> DTO
    public static SpaceDto toDto(Space space) {
        return SpaceDto.builder()
                .sname(space.getSname())
                .sthumb(space.getSthumb())
                .is_primary(space.getIs_primary())
                .is_public(space.getIs_public())
                .created_at(space.getCreated_at())
                .build();
    }

}
