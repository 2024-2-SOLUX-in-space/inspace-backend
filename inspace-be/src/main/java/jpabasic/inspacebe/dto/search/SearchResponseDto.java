package jpabasic.inspacebe.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponseDto {

    private List<Map<String, String>> images; // 이미지 검색 결과
    private List<Map<String, String>> videos; // 유튜브 검색 결과
    private List<Map<String, String>> music;  // Spotify 검색 결과

    public SearchResponseDto() {}

    public SearchResponseDto(List<Map<String, String>> images, List<Map<String, String>> videos, List<Map<String, String>> music) {
        this.images = images;
        this.videos = videos;
        this.music = music;
    }


    public List<Map<String, String>> getImages() {
        return images;
    }

    public void setImages(List<Map<String, String>> images) {
        this.images = images;
    }

    public List<Map<String, String>> getVideos() {
        return videos;
    }

    public void setVideos(List<Map<String, String>> videos) {
        this.videos = videos;
    }

    public List<Map<String, String>> getMusic() { return music; }

    public void setMusic(List<Map<String, String>> music) { this.music = music; }

    @Override
    public String toString() {
        return "SearchResponseDto{" +
                "images=" + images +
                ", videos=" + videos +
                ", music=" + music +
                '}';
    }
}
