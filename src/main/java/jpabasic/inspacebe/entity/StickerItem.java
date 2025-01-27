package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="sticker_content")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StickerItem {

    @Id
    @Column(name="item_id",nullable = false)
    private String itemId;

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


    @OneToOne
    @JoinColumn(name="item_id",referencedColumnName = "item_id")
    private Item item;


}
