package jpabasic.inspacebe.dto.item;

public class ItemRequestDto {
    private String itemId; // 검색 키
    private Integer uid;   // 사용자 ID
    private Integer spaceId; // 공간 ID

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

}
