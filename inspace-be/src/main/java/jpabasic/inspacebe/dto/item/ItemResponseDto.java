package jpabasic.inspacebe.dto.item;

import jpabasic.inspacebe.entity.CType;

public class ItemResponseDto {
    private String itemId;
    private String title;
    private String imageUrl;
    private String contentsUrl;
    private CType ctype;
    private Integer spaceId;
    private Integer uid;

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

    public String getContentsUrl() {
        return contentsUrl;
    }

    public void setContentsUrl(String contentsUrl) {
        this.contentsUrl = contentsUrl;
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
                '}';
    }
}
