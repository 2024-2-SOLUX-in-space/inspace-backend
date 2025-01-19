package jpabasic.inspacebe.dto;

import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.entity.User;
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
//    private User user;
    private String sname;
    private int sthumb;
    private Boolean isPrimary=false;
    private Boolean isPublic=false;

    @CurrentTimestamp
    private LocalDateTime createdAt;

    //Entity ->DTO
    public static SpaceDto toDto(Space space) {
        return SpaceDto.builder()
                .sname(space.getSname())
                .sthumb(space.getSthumb())
                .isPrimary(space.getIsPrimary())
                .isPublic(space.getIsPublic())
                .createdAt(space.getCreatedAt())
                .build();
    }

}
