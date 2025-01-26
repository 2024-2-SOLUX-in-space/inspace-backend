package jpabasic.inspacebe.dto;

import jpabasic.inspacebe.entity.Space;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceDto {

    //    private User user;
    private Integer spaceId;
    private String sname;
    private int sthumb;
    private Boolean isPrimary = false;
    private Boolean isPublic;


    private LocalDateTime createdAt;

    //Entity ->DTO
    public static SpaceDto toDto(Space space) {
        return SpaceDto.builder()
                .spaceId(space.getSpaceId())
                .sname(space.getSname())
                .sthumb(space.getSthumb())
                .isPrimary(space.getIsPrimary())
                .isPublic(space.getIsPublic())
                .createdAt(space.getCreatedAt())
                .build();
    }

}
