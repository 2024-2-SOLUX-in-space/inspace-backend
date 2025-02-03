package jpabasic.inspacebe.service.search;

import jakarta.transaction.Transactional;
import jpabasic.inspacebe.config.SpotifyConfig;
import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.util.*;

@Service
public class SearchService {

    private final ItemRepository itemRepository;

    private final SpaceRepository spaceRepository;

    private final SpotifyApi spotifyApi;

    private final WebClient webClient;
    private final SpotifyConfig spotifyConfig;

    private final Map<String, Item> cachedItems = new HashMap<>();
    private final Map<String, Map<String, Object>> crawledItemCache = new HashMap<>();


    public SearchService(ItemRepository itemRepository, SpaceRepository spaceRepository, WebClient.Builder webClientBuilder, SpotifyApi spotifyApi, SpotifyConfig spotifyConfig) {
        this.itemRepository = itemRepository;
        this.spaceRepository = spaceRepository;
        this.spotifyApi = spotifyApi;
        this.spotifyConfig = spotifyConfig;
        this.webClient = webClientBuilder.build();
    }

    // Crawled 데이터를 위한 메서드
    public Optional<Map<String, Object>> getCrawledItemCache(String id) {
        return Optional.ofNullable(crawledItemCache.get(id));
    }

    // Item 객체를 위한 메서드
    public Optional<Item> getCachedItem(String id) {
        return Optional.ofNullable(cachedItems.get(id));
    }

    // Crawled 데이터 추가
    public void addCrawledItem(String id, Map<String, Object> data) {
        crawledItemCache.put(id, data);
    }

    // Item 캐시 추가
    public void addCachedItem(String id, Item item) {
        cachedItems.put(id, item);
    }

    // Crawled 데이터 제거
    public void removeCrawledItem(String id) {
        crawledItemCache.remove(id);
    }

    // Item 캐시 제거
    public void removeCachedItem(String id) {
        cachedItems.remove(id);
    }

    @Value("${google.api-key}")
    private String apiKey;

    @Value("${google.search-engine-id}")
    private String searchEngineId;

    @Value("${youtube.api-key}")
    private String youtubeApiKey;

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;


    public Map<String, Object> searchAll(String query, List<String> filters) {
        Map<String, Object> results = new HashMap<>();
        System.out.println("[soyg] Received search request: query=" + query + ", filters=" + filters);

        List<Map<String, Object>> images = searchImages(query);
        List<Map<String, Object>> videos = searchYouTube(query);
        List<Map<String, Object>> music = searchSpotify(query);
        List<Map<String, Object>> spaces = searchSpaces(query);

        if (filters == null || filters.isEmpty()) {
            results.put("images", images);
            results.put("videos", videos);
            results.put("music", music);
            results.put("spaces", spaces);
            return results;
        }

        if (filters.contains("image")) {
            results.put("images", images);
        }
        if (filters.contains("youtube")) {
            results.put("videos", videos);
        }
        if (filters.contains("music")) {
            results.put("music", music);
        }
        if (filters.contains("space")) {
            results.put("spaces", spaces);
        }

        System.out.println("[soyg] Final results: " + results);
        return results;
    }

    public List<Map<String, Object>> searchImages(String query) {
        String url = String.format("/api/proxy/search?service=image&query=%s", query);
        Map<String, Object> response;

        try {
            response = webClient.get().uri(url).retrieve().bodyToMono(Map.class).block();
            System.out.println("[soyg] Response from Proxy (Images): " + response);
        } catch (Exception e) {
            System.err.println("[soyg] Error during Proxy request (Images): " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // 에러 발생 시 빈 리스트 반환
        }

        List<Map<String, Object>> results = new ArrayList<>();

        //  업로드된 이미지 추가
        List<Item> uploadedItems = itemRepository.findUploadedItems(query);
        for (Item item : uploadedItems) {
            Map<String, Object> imageData = new HashMap<>();
            imageData.put("title", item.getTitle());
            imageData.put("imageUrl", item.getImageUrl());
            imageData.put("itemId", item.getItemId());
            imageData.put("isUploaded", true);
            imageData.put("ctype", CType.IMAGE);
            results.add(imageData);
        }

        //  크롤링된 이미지 추가
        if (response != null && response.containsKey("items")) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> imageData = new HashMap<>();
                imageData.put("title", item.get("title"));
                imageData.put("contentUrl", item.get("link"));
                imageData.put("imageUrl", item.get("link"));
                imageData.put("isUploaded", false);
                imageData.put("ctype", CType.IMAGE);

//                // 이미지 URL 설정
//                String imageUrl = null;
//                if (item.containsKey("pagemap")) {
//                    Map<String, Object> pagemap = (Map<String, Object>) item.get("pagemap");
//                    if (pagemap.containsKey("cse_image")) {
//                        List<Map<String, String>> cseImageList = (List<Map<String, String>>) pagemap.get("cse_image");
//                        if (!cseImageList.isEmpty()) {
//                            imageUrl = cseImageList.get(0).get("src");
//                        }
//                    }
//                }
//                if (imageUrl == null && item.containsKey("metatags")) {
//                    List<Map<String, Object>> metatags = (List<Map<String, Object>>) item.get("metatags");
//                    if (!metatags.isEmpty() && metatags.get(0).containsKey("og:image")) {
//                        imageUrl = (String) metatags.get(0).get("og:image");
//                    }
//                }
//                imageData.put("imageUrl", imageUrl != null ? imageUrl : "");

                // UUID 생성 후 캐시에 저장
                String uuid = UUID.randomUUID().toString();
                imageData.put("itemId", uuid);
                crawledItemCache.put(uuid, imageData); // 캐시에 저장

                results.add(imageData);
            }
        } else {
            System.err.println("[soyg] Google API 응답이 'items' 키를 포함하지 않음: " + response);
        }

        return results;
    }


    public List<Map<String, Object>> searchYouTube(String query) {
        String url = String.format("/api/proxy/search?service=youtube&query=%s&type=video", query);

        Map<String, Object> response;
        try {
            response = webClient.get().uri(url).retrieve().bodyToMono(Map.class).block();
        } catch (Exception e) {
            System.err.println("Error during Proxy request (YouTube): " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }

        List<Map<String, Object>> results = new ArrayList<>();
        if (response != null && response.containsKey("items")) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> videoData = new HashMap<>();
                Map<String, Object> id = (Map<String, Object>) item.get("id");

                // 비디오 ID 추출
                String videoId = id != null ? (String) id.get("videoId") : null;
                if (videoId == null) continue;

                // snippet 정보 추출
                Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
                if (snippet == null) continue;

                String title = (String) snippet.get("title");
                Map<String, Object> thumbnails = (Map<String, Object>) snippet.get("thumbnails");
                String thumbnailUrl = null;

                if (thumbnails != null) {
                    Map<String, String> defaultThumbnail = (Map<String, String>) thumbnails.get("default");
                    thumbnailUrl = defaultThumbnail != null ? defaultThumbnail.get("url") : null;
                }

                // 비디오 데이터 추가
                videoData.put("title", title);
                videoData.put("contentUrl", "https://www.youtube.com/watch?v=" + videoId);
                videoData.put("imageUrl", thumbnailUrl != null ? thumbnailUrl : "");
                videoData.put("isUploaded", false);
                videoData.put("ctype", CType.YOUTUBE);

                Map<String, Object> detailsResponse;
                try {
                    String detailsUrl = String.format("/api/proxy/search?service=youtube-details&videoId=%s", videoId);
                    detailsResponse = webClient.get().uri(detailsUrl).retrieve().bodyToMono(Map.class).block();
                } catch (Exception e) {
                    System.err.println("Error during Proxy request (YouTube Details): " + e.getMessage());
                    detailsResponse = null;
                }

                if (detailsResponse != null && detailsResponse.containsKey("items")) {
                    List<Map<String, Object>> detailsItems = (List<Map<String, Object>>) detailsResponse.get("items");

                    if (!detailsItems.isEmpty()) {
                        Map<String, Object> contentDetails = (Map<String, Object>) detailsItems.get(0).get("contentDetails");
                        if (contentDetails != null) {
                            String duration = (String) contentDetails.get("duration"); // ISO 8601 형식 (ex. PT1H2M3S)
                            System.out.println("Original Duration: " + duration);

                            // 재생시간 변환 (ISO 8601 Duration 형식 -> 초 단위 변환)
                            Integer ytbDur = parseYouTubeDuration(duration);
                            videoData.put("ytbDur", ytbDur);
                        }
                    }
                } else {
                    videoData.put("ytbDur", 0); // 기본값 설정
                }

                // UUID 생성 후 캐시에 저장
                String uuid = UUID.randomUUID().toString();
                videoData.put("itemId", uuid);
                crawledItemCache.put(uuid, videoData); // 캐시에 저장

                results.add(videoData);
            }
        }

        return results;
    }

    public Integer parseYouTubeDuration(String duration) {
        if (duration == null || duration.isEmpty()) {
            return 0;
        }

        int hours = 0, minutes = 0, seconds = 0;
        try {
            duration = duration.replace("PT", "");

            if (duration.contains("H")) {
                String[] split = duration.split("H");
                hours = Integer.parseInt(split[0]);
                duration = split.length > 1 ? split[1] : "";
            }

            if (duration.contains("M")) {
                String[] split = duration.split("M");
                minutes = Integer.parseInt(split[0]);
                duration = split.length > 1 ? split[1] : "";
            }

            if (duration.contains("S")) {
                seconds = Integer.parseInt(duration.replace("S", ""));
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid duration format: " + duration);
            return 0;
        }

        return hours * 3600 + minutes * 60 + seconds;
    }


    public List<Map<String, Object>> searchSpotify(String query) {
        List<Map<String, Object>> results = new ArrayList<>();

        String accessToken = spotifyConfig.getAccessToken(spotifyApi);
        if (accessToken == null || accessToken.isEmpty()) {
            System.err.println("[Spotify] Failed to get a valid Access Token.");
            return results;
        }
        spotifyApi.setAccessToken(accessToken);

        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query).limit(5).build();
        try {
            Paging<Track> trackPaging = searchTracksRequest.execute();
            Track[] tracks = trackPaging.getItems();

            for (Track track : tracks) {
                Map<String, Object> trackData = new HashMap<>();
                trackData.put("title", track.getName());
                trackData.put("url", track.getExternalUrls().get("spotify"));
                trackData.put("artist", track.getArtists()[0].getName());

                // 앨범 이미지 추가
                if (track.getAlbum().getImages().length > 0) {
                    trackData.put("thumbnail", track.getAlbum().getImages()[0].getUrl());
                } else {
                    trackData.put("thumbnail", ""); // 기본 이미지 없음 처리
                }

                trackData.put("isUploaded", false);
                trackData.put("ctype", CType.MUSIC);

                // UUID 생성 후 캐시에 저장
                String uuid = UUID.randomUUID().toString();
                trackData.put("itemId", uuid);
                crawledItemCache.put(uuid, trackData); // 캐시에 저장

                results.add(trackData);
            }

        } catch (IOException | se.michaelthelin.spotify.exceptions.SpotifyWebApiException |
                 org.apache.hc.core5.http.ParseException e) {
            System.err.println("Error fetching Spotify search results: " + e.getMessage());
        }

        return results;
    }
    @Transactional
    public List<Map<String, Object>> searchSpaces(String query) {
        List<Space> spaces = spaceRepository.findAll();
        List<Map<String, Object>> results = new ArrayList<>();

        for (Space space : spaces) {
            if (space.getSname() != null && space.getSname().contains(query)) {
                Map<String, Object> spaceData = new HashMap<>();

                //첫번째 page 출력
                Integer page=getFirstPageId(space);

                List<Item> items=space.getItems();
                List<ArchiveRequestDto> itemDtos=items.stream()
                        .filter(item -> Objects.equals(item.getPageId(),page))
                        .map(ArchiveRequestDto::toArchiveDto)
                        .toList();




                spaceData.put("spaceId", space.getSpaceId());
                spaceData.put("sname", space.getSname());
                spaceData.put("userId", space.getUser().getUserId());
                spaceData.put("isPublic", space.getIsPublic());
                spaceData.put("createdAt", space.getCreatedAt());
                spaceData.put("item",itemDtos);
                spaceData.put("ctype", CType.SPACE);

                results.add(spaceData);
            }
        }

        return results;
    }

    @Transactional
    public Integer getFirstPageId(Space space) {
        return space.getPages().getFirst().getPageId();
    }

}






