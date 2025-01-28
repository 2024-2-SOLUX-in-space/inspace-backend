package jpabasic.inspacebe.dto.item;


import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
/// 저장소에서 각 아이템 조회 시
public class ItemsDto {
    private String itemId;
    private String title;
    private CType ctype;
    private String imageUrl;
    private Float height;
    private Float width;

    public ItemsDto(Item item) {
    }

    //Entity->toDto
    public static ItemsDto toDto(Item item) {
        return ItemsDto.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .imageUrl(item.getImageUrl())
                .ctype(item.getCtype())
                .height(item.getHeight())
                .width(item.getWidth())
                .build();

    }

}
