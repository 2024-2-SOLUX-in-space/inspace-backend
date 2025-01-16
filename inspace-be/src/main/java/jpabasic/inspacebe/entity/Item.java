package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.ImageItem;
import jpabasic.inspacebe.entity.MusicItem;
import jpabasic.inspacebe.entity.YoutubeItem;
import lombok.*;

@Entity
@Table(name = "\"item\"")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "ctype",columnDefinition = "varchar(10)")
    private CType ctype;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "contents_url", columnDefinition = "TEXT")
    private String contentsUrl;

    @Column(name = "position_x")
    private Float positionX;

    @Column(name = "position_y")
    private Float positionY;

    @Column(name = "height") //추가
    private Float height;

    @Column(name = "width") //추가
    private Float width;

    @Column(name = "turnover") //추가
    private Float turnover;

    //이미지가 겹쳐져있는 경우 순서
    @Column(name="order") //추가
    private Integer order;

//    @Column(name = "size", columnDefinition = "json")
//    private String size;

    @Column(name = "is_uploaded", columnDefinition = "TINYINT(1)") // TINYINT(1) 사용
    private Boolean isUploaded;

//    @ManyToOne
//    @JoinColumn(name = "space_id", referencedColumnName = "space_id", nullable = false)
//    private Space space;
//
//    @ManyToOne
//    @JoinColumn(name = "uid", referencedColumnName = "user_id", nullable = true) // 유저와의 연결 선택적
//    private User user;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private YoutubeItem youTubeItem;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private ImageItem imageItem;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private MusicItem musicItem;

    @ManyToOne
    @JoinColumn(name = "page_id",referencedColumnName = "page_id",nullable = false)
    private Page page;  // Page와의 관계를 설정하는 필드

    public Item(String title, CType ctype, String imageUrl, Boolean isUploaded, Page page) {
        this.title = title;
        this.ctype = ctype;
        this.imageUrl = imageUrl;
        this.isUploaded = isUploaded;
//        this.space = space;
        this.page = page;
    }

    public void setSpaceId(Integer spaceId) {
        Space space = new Space();
        space.setSpaceId(spaceId); // Space 객체에 ID 설정
//        this.space = space;
    }

    public void setUid(Integer uid) {
        User user = new User();
        user.setUserId(uid); // User 객체에 ID 설정
//        this.user = user;
    }

//    public Integer getSpaceId() {
//        return this.space != null ? this.space.getSpaceId() : null;
//    }

//    public Integer getUid() {
//        return this.user != null ? this.user.getUserId() : null;
//    }
}

