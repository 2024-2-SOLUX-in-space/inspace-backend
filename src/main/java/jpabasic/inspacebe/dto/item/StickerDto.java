package jpabasic.inspacebe.dto.item;

import io.swagger.v3.oas.annotations.info.Info;
import jpabasic.inspacebe.entity.StickerItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter

public class StickerDto {

    //id
//    private String stickerId;
    private String title;
    private String src;
    private String alt;
    private String color;

    //StickerDto -> StickerItem 엔티티
    public static StickerItem toEntity(StickerDto dto){
        return StickerItem.builder()
//                .stickerItemId(dto.getStickerId())
                .title(dto.getTitle())
                .src(dto.getSrc())
                .alt(dto.getSrc())
                .color(dto.getColor())
                .build();
    }


}
