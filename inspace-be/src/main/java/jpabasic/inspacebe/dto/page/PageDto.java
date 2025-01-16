package jpabasic.inspacebe.dto.page;


import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Page;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PageDto {

    private int pageNumber;
    private List<Item> items;

    public static PageDto toDto(Page page){
        return PageDto.builder()
                .pageNumber(page.getPageNumber())
                .items(page.getItems())
                .build();
    }
}
