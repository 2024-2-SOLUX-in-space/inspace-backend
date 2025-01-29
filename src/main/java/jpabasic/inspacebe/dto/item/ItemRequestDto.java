package jpabasic.inspacebe.dto.item;

public class ItemRequestDto {
    private String itemId; // 검색 키
    private Integer spaceId; // 공간 ID

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

}
