package jpabasic.inspacebe.dto.item;

import jpabasic.inspacebe.entity.CType;

public class ItemResponseDto {
    private String itemId;
    private String title;
    private String imageUrl;
    private Boolean isUploaded;
    private Integer userId;
    private String userName;
    private CType ctype;

    // Getters and Setters
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

    public Boolean getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(Boolean isUploaded) {
        this.isUploaded = isUploaded;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CType getCtype() {
        return ctype;
    }

    public void setCtype(CType ctype) {
        this.ctype = ctype;
    }


}
