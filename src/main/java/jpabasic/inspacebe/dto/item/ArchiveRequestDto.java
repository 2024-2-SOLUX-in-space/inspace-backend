package jpabasic.inspacebe.dto.item;

import io.swagger.v3.core.util.Json;
import jpabasic.inspacebe.entity.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArchiveRequestDto {


    private String itemId;
    private String title; //sticker =null
    private String imageUrl; //sticker=null
    private String contentsUrl;//sticker=null
    private CType ctype;
    private Float positionX;
    private Float positionY;
    private Float height;
    private Float width;
    private Float turnover;
    private Integer sequence;
    private StickerDto sticker; //sticker 제외 나머지 item 은 null

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
                .sticker(item.getStickerItem() != null ? StickerItem.toDto(item.getStickerItem()) : null) // null 처리 추가
                .build();
    }

    public static ArchiveRequestStickerDto toStickerRequestDto(ArchiveRequestDto dto) {
        return ArchiveRequestStickerDto.builder()
                .itemId(dto.getItemId())
                .sticker(dto.getSticker())
                .ctype(dto.getCtype())
                .positionX(dto.getPositionX())
                .positionY(dto.getPositionY())
                .height(dto.getHeight())
                .width(dto.getWidth())
                .turnover(dto.getTurnover())
                .sequence(dto.getSequence())
                .build();
    }







}
