package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "music_content")
@Getter
@Setter
@NoArgsConstructor
public class MusicItem {

    @Id
    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "music_artist", length = 255)
    private String musicArtist;

    @Column(name = "music_thumb", columnDefinition = "TEXT")
    private String musicThumb;

    @Column(name = "music_url", columnDefinition = "TEXT")
    private String musicUrl;

    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", insertable = false, updatable = false)
    private Item item;
}