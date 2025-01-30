package jpabasic.inspacebe.dto.item;

import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArchiveResponseDto {

    private String itemId;
    private String title;
    private String imageUrl;
    private String contentsUrl;

    private String userId;
    private CType ctype;
    private Float positionX;
    private Float positionY;
    private Float height;
    private Float width;
    private Float turnover;
    private Integer sequence;

    //DTO->Entity
    public static Item toEntity(ArchiveResponseDto archiveDto) {
        return Item.builder()
                .itemId(archiveDto.getItemId())
//                .title(archiveDto.getTitle())
//                .imageUrl(archiveDto.getImageUrl())
//                .contentsUrl(archiveDto.getContentsUrl())
                .ctype(archiveDto.getCtype())

                .positionX(archiveDto.getPositionX())
                .positionY(archiveDto.getPositionY())
                .height(archiveDto.getHeight())
                .width(archiveDto.getWidth())
                .turnover(archiveDto.getTurnover())
                .sequence(archiveDto.getSequence())
                .build();
    }
}
