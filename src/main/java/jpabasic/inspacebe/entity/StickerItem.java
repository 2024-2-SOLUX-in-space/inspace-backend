package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import jpabasic.inspacebe.dto.item.StickerDto;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="sticker_item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerItem {

    @Id
    @Column(name="stickerItem_id",nullable = false)
    private String stickerItemId= UUID.randomUUID().toString();

    //front 상에서의 id //"sticker-0" 형태
    @Column(name="title",nullable = false,unique = true)
    private String title;


    @Column(name="src",columnDefinition = "TEXT")
    private String src;

    //alt
    @Column(name="alt")
    private String alt;

    @Column(name="color")
    private String color;


//    @OneToMany
//    @JoinColumn(name="item_id",referencedColumnName = "item_id")
//    private Item item;

    public static StickerDto toDto(StickerItem item) {
        return StickerDto.builder()
                .title(item.getTitle())
                .src(item.getSrc())
                .alt(item.getAlt())
                .color(item.getColor())
                .build();
    }


}
