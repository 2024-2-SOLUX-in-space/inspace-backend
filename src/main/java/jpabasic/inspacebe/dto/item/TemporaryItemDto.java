package jpabasic.inspacebe.dto.item;

public class TemporaryItemDto {

    private String uuid;
    private String title;
    private String imageUrl;
    private String contentsUrl;

    public TemporaryItemDto(String uuid, String title, String imageUrl, String contentsUrl) {
        this.uuid = uuid;
        this.title = title;
        this.imageUrl = imageUrl;
        this.contentsUrl = contentsUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
