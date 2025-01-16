package jpabasic.inspacebe.dto.item;

import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Page;
import jpabasic.inspacebe.entity.Space;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArchiveRequestDto {

    private String title;
    private String imageUrl;
    private String contentsUrl;
    private CType ctype;
    private Float positionX;
    private Float positionY;
    private Float height;
    private Float width;
    private Float turnover;
    private Integer order;

    //DTO->Entity
    public static Item toEntity(ArchiveRequestDto archiveDto) {
        return Item.builder()
                .title(archiveDto.getTitle())
                .imageUrl(archiveDto.getImageUrl())
                .contentsUrl(archiveDto.getContentsUrl())
                .ctype(archiveDto.getCtype())
//                .spaceId(archiveDto.getSpaceId())
//                .userId(archiveDto.getUserId())
                .positionX(archiveDto.getPositionX())
                .positionY(archiveDto.getPositionY())
                .height(archiveDto.getHeight())
                .width(archiveDto.getWidth())
                .turnover(archiveDto.getTurnover())
                .order(archiveDto.getOrder())
                .build();
    }




}
