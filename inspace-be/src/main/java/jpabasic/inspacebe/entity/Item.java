<<<<<<< HEAD
package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "title", length = 10)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "ctype")
    private CType ctype;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "contents_url", columnDefinition = "TEXT")
    private String contentsUrl;

    @Column(name = "position_x")
    private Float positionX;

    @Column(name = "position_y")
    private Float positionY;

    @Column(name = "size", columnDefinition = "json")
    private String size;

    @Column(name = "is_uploaded", columnDefinition = "TINYINT(1)") // TINYINT(1) 사용
    private Boolean isUploaded;

    @ManyToOne
    @JoinColumn(name = "space_id", referencedColumnName = "space_id", nullable = false)
    private Space space;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "user_id", nullable = true) // 유저와의 연결 선택적
    private User user;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private YoutubeItem youTubeItem;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private ImageItem imageItem;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private MusicItem musicItem;

    public Item(String title, CType ctype, String imageUrl, Boolean isUploaded, Space space) {
        this.title = title;
        this.ctype = ctype;
        this.imageUrl = imageUrl;
        this.isUploaded = isUploaded;
        this.space = space;
    }

    public void setSpaceId(Integer spaceId) {
        Space space = new Space();
        space.setSpaceId(spaceId); // Space 객체에 ID 설정
        this.space = space;
    }

    public void setUid(Integer uid) {
        User user = new User();
        user.setUserId(uid); // User 객체에 ID 설정
        this.user = user;
    }

    public Integer getSpaceId() {
        return this.space != null ? this.space.getSpaceId() : null;
    }

    public Integer getUid() {
        return this.user != null ? this.user.getUserId() : null;
    }
}
=======
package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import jpabasic.inspacebe.converter.CtypeAttributeConverter;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int item_id;
    private String title;
    @Convert(converter= CtypeAttributeConverter.class)
    private String ctype;
    private String image_url;
    private String contents_url;
    private float position_x;
    private float position_y;
    private float height;
    private float width;

}
>>>>>>> e799886927a0899c21fc59810cc494b5cdd71bf4
