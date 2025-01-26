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

    @Column(name="src",columnDefinition = "TEXT")
    private String src;


    @OneToOne
    @JoinColumn(name="item_id",referencedColumnName = "item_id")
    private Item item;

}
