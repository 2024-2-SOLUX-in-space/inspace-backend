package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_content")
@Getter
@Setter
@NoArgsConstructor
public class ImageItem {

    @Id
    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "img_url", columnDefinition = "TEXT")
    private String imgUrl;

    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", insertable = false, updatable = false)
    private Item item;
}