package jpabasic.inspacebe.dto.item;

import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ItemResponseDto {
    private String itemId;
    private String title;
    private String imageUrl;
    private Integer userId;
    private String userName;
    private CType ctype;
    private String contentsUrl;
    private Boolean isUploaded;
    private Integer spaceId;
    private Integer uid;

    // YouTube-specific fields
    private String ytbUrl;
    private String ytbThumb;
    private Integer ytbDur;

    // Music-specific fields
    private String musicUrl;
    private String musicThumb;
    private String musicArtist;

    public ItemResponseDto() {
    }

    public ItemResponseDto(String itemId, String title, String imageUrl, String contentsUrl, CType ctype, Integer spaceId, Integer uid) {
        this.itemId = itemId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.contentsUrl = contentsUrl;
        this.ctype = ctype;
        this.spaceId = spaceId;
        this.uid = uid;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public CType getCtype() {
        return ctype;
    }

    public void setCtype(CType ctype) {
        this.ctype = ctype;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }


    @Override
    public String toString() {
        return "ItemResponseDto{" +
                "itemId='" + itemId + '\'' +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", contentsUrl='" + contentsUrl + '\'' +
                ", ctype=" + ctype +
                ", spaceId=" + spaceId +
                ", uid=" + uid +
                ", ytbUrl='" + ytbUrl + '\'' +
                ", ytbThumb='" + ytbThumb + '\'' +
                ", ytbDur=" + ytbDur +
                ", musicUrl='" + musicUrl + '\'' +
                ", musicThumb='" + musicThumb + '\'' +
                ", musicArtist='" + musicArtist + '\'' +
                '}';
    }

    //Entity->toDto
    public static ItemResponseDto toDto(Item item) {
        return ItemResponseDto.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .imageUrl(item.getImageUrl())
                .contentsUrl(item.getContentsUrl())
                .ctype(item.getCtype())
                .userName(item.getUser() != null ? item.getUser().getName() : "Crawled Source")
                // YouTube-specific fields
                .ytbUrl(item.getYoutubeItem() != null ? item.getYoutubeItem().getYtbUrl() : null)
                .ytbThumb(item.getYoutubeItem() != null ? item.getYoutubeItem().getYtbThumb() : null)
                .ytbDur(item.getYoutubeItem() != null ? item.getYoutubeItem().getYtbDur() : null)
                // Music-specific fields
                .musicUrl(item.getMusicItem() != null ? item.getMusicItem().getMusicUrl() : null)
                .musicThumb(item.getMusicItem() != null ? item.getMusicItem().getMusicThumb() : null)
                .musicArtist(item.getMusicItem() != null ? item.getMusicItem().getMusicArtist() : null)
                .build();

    }
}
