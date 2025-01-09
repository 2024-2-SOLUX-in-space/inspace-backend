package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "youtube_content")
@Getter
@Setter
@NoArgsConstructor
public class YoutubeItem {

    @Id
    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "ytb_url", columnDefinition = "TEXT")
    private String ytbUrl;

    @Column(name = "ytb_thumb", columnDefinition = "TEXT")
    private String ytbThumb;

    @Column(name = "ytb_dur")
    private Integer ytbDur;

    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", insertable = false, updatable = false)
    private Item item;
}