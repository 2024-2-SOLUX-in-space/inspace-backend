package jpabasic.inspacebe.dto.page;


import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.entity.Item;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {

    private int pageNumber;
    private List<ItemResponseDto> items;


    public static List<ItemResponseDto> getItemList(List<Item> items) {
        List<ItemResponseDto> responseDtos = items.stream()
                .map(ItemResponseDto::toDto)//각 Item을 ItemResponseDto로 변환.
                .collect(Collectors.toList()); //결과를 리스트로 수집
        return responseDtos;
    }


}
