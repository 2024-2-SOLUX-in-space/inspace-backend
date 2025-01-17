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


    private String itemId;
    private String title;
    private String imageUrl;
    private String contentsUrl;
    private CType ctype;
    private Float positionX;
    private Float positionY;
    private Float height;
    private Float width;
    private Float turnover;
    private Integer sequence;
//    private User

    //DTO->Entity
    public static Item toEntity(ArchiveRequestDto archiveDto) {
        return Item.builder()
                .itemId(archiveDto.getItemId())
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
                .sequence(archiveDto.getSequence())
                .build();
    }

    //item->ArchiveRequestDto
    //페이지 조회에 사용
    public static ArchiveRequestDto toArchiveDto(Item item) {
        return ArchiveRequestDto.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .imageUrl(item.getImageUrl())
                .contentsUrl(item.getContentsUrl())
                .ctype(item.getCtype())
                .positionX(item.getPositionX())
                .positionY(item.getPositionY())
                .width(item.getWidth())
                .turnover(item.getTurnover())
                .sequence(item.getSequence())
                .build();
    }







}
