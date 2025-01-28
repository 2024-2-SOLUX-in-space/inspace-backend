package jpabasic.inspacebe.dto.item;

import io.swagger.v3.oas.annotations.info.Info;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter

public class StickerDto {

    //id
    private String title;
    private String src;
    private String alt;
    private String color;


}
