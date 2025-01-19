package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.ImageItem;
import jpabasic.inspacebe.entity.MusicItem;
import jpabasic.inspacebe.entity.YoutubeItem;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id //민서 수정
//    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id", nullable = false)
    private String itemId=UUID.randomUUID().toString();

    @Column(name = "title", length = 10)
    private String title;


    @Enumerated(EnumType.STRING)
    @Column(name = "ctype")
    private CType ctype;

    //클라우드에 저장된 경로.
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    //해당 아이템의 상세 정보 //youtube의 경우 해당 경로.
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
    @Column(name="sequence") //추가
    private Integer sequence;

//    @Column(name = "size", columnDefinition = "json")
//    private String size;

    //사용자가 직접 올린 사진인지의 여부.
    @Column(name = "is_uploaded", columnDefinition = "TINYINT(1)") // TINYINT(1) 사용
    private Boolean isUploaded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "user_id", nullable = true)
    private User user;


    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private YoutubeItem youTubeItem;

    @OneToOne(mappedBy = "item",cascade = CascadeType.ALL)
    private ImageItem imageItem;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private MusicItem musicItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id",referencedColumnName = "page_id")
    private Page page;  // Page와의 관계를 설정하는 필드


    public CType getCtype() {
        return ctype;
    }

    public Item(String title, CType ctype, String imageUrl, Boolean isUploaded, Space space) {
        this.title = title;
        this.ctype = ctype;
        this.imageUrl = imageUrl;
        this.space = space;
    }

    // spaceId 관련 메서드
    public void setSpaceId(Integer spaceId) {
        if (this.space == null) {
            this.space = new Space();
        }
        this.space.setSpaceId(spaceId);
    }

    public Integer getSpaceId() {
        return this.space != null ? this.space.getSpaceId() : null;
    }

    // pageId 관련 메서드
    public void setPageId(Integer pageId) {
        if (this.page == null) {
            this.page = new Page();
        }
        this.page.setPageId(pageId);
    }

    public Integer getPageId() {
        return this.page != null ? this.page.getPageId() : null;
    }

    // userId (uid) 관련 메서드
    public void setUid(Integer uid) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setUserId(uid);
    }

    public Integer getUid() {
        return this.user != null ? this.user.getUserId() : null;
    }


}

