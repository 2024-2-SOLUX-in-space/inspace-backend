package jpabasic.inspacebe.service.search;

import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class SearchService {

    private final ItemRepository itemRepository;

    private final SpaceRepository spaceRepository;

    private final WebClient webClient;

    @Value("${server.proxy-url}") // 프록시 서버 URL 추가
    private String proxyUrl;


    private final Map<String, Item> cachedItems = new HashMap<>();
    private final Map<String, Map<String, Object>> crawledItemCache = new HashMap<>();


    public SearchService(ItemRepository itemRepository, SpaceRepository spaceRepository, WebClient.Builder webClientBuilder) {
        this.itemRepository = itemRepository;
        this.spaceRepository = spaceRepository;
        this.webClient = webClientBuilder.baseUrl(proxyUrl).build();
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

        if (filters == null || filters.isEmpty()) {
            results.put("images", searchImages(query));
            results.put("uploadedImages", searchUserUploadedImages(query));
            results.put("videos", searchYouTube(query));
            results.put("music", searchSpotify(query));
            results.put("spaces", searchSpaces(query));
            return results;
        }

        if (filters.contains("image")) {
            List<Map<String, Object>> combinedImages = new ArrayList<>();
            combinedImages.addAll(searchImages(query));
            combinedImages.addAll(searchUserUploadedImages(query));
            results.put("images", combinedImages);
        }
        if (filters.contains("youtube")) {
            results.put("videos", searchYouTube(query));
        }
        if (filters.contains("music")) {
            results.put("music", searchSpotify(query));
        }
        if (filters.contains("space")) {
            results.put("spaces", searchSpaces(query)); // Space 검색 결과 추가
        }
        return results;
    }

    public List<Map<String, Object>> searchUserUploadedImages(String query) {
        List<Item> items = itemRepository.findUploadedItems(query);

        List<Map<String, Object>> results = new ArrayList<>();
        for (Item item : items) {
            Map<String, Object> imageData = new HashMap<>();
            imageData.put("title", item.getTitle());
            imageData.put("imageUrl", item.getImageUrl());
            imageData.put("itemId", item.getItemId());
            imageData.put("isUploaded", true);
            imageData.put("ctype", CType.IMAGE);
            results.add(imageData);
        }

        return results;
    }

    public List<Map<String, Object>> searchImages(String query) {
        String url = String.format("%s/api/proxy/search?service=google&query=%s", proxyUrl, query);
        Map<String, Object> response;

        try {
            response = webClient.get().uri(url).retrieve().bodyToMono(Map.class).block();
            System.out.println("Response from Proxy (Images): " + response); // 응답 로그 추가
        } catch (Exception e) {
            System.err.println("Error during Proxy request (Images): " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // 에러 발생 시 빈 리스트 반환
        }


        List<Map<String, Object>> results = new ArrayList<>();
        if (response != null && response.containsKey("items")) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> imageData = new HashMap<>();
                imageData.put("title", item.get("title"));
                Map<String, String> image = (Map<String, String>) item.get("image");
                imageData.put("imageUrl", image.get("thumbnailLink"));
                imageData.put("contentUrl", item.get("link"));
                imageData.put("isUploaded", false);
                imageData.put("ctype", CType.IMAGE);

                String uuid = UUID.randomUUID().toString();
                imageData.put("itemId", uuid);
                crawledItemCache.put(uuid, imageData); // 캐시에 저장

                results.add(imageData);
            }
        }

        return results;
    }

    public List<Map<String, Object>> searchYouTube(String query) {
        String url = String.format("%s/api/proxy/search?service=youtube&query=%s", proxyUrl, query);

        System.out.println("Proxy URL (YouTube): " + url);

        Map<String, Object> response;
        try {
            response = webClient.get().uri(url).retrieve().bodyToMono(Map.class).block();
            System.out.println("Response from Proxy (YouTube): " + response); // 응답 로그 추가
        } catch (Exception e) {
            System.err.println("Error during Proxy request (YouTube): " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // 에러 발생 시 빈 리스트 반환
        }
        List<Map<String, Object>> results = new ArrayList<>();
        if (response != null && response.containsKey("items")) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> videoData = new HashMap<>();
                Map<String, Object> id = (Map<String, Object>) item.get("id");

                // 동영상 ID 가져오기
                String videoId = (String) id.get("videoId");

                // 재생시간 가져오기 위한 API 호출
                String detailsUrl = String.format(
                        "https://www.googleapis.com/youtube/v3/videos?part=contentDetails&id=%s&key=%s",
                        videoId, youtubeApiKey
                );

                Map<String, Object> detailsResponse = webClient.get()
                        .uri(detailsUrl)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

                String duration = null;
                if (detailsResponse != null && detailsResponse.containsKey("items")) {
                    List<Map<String, Object>> detailsItems = (List<Map<String, Object>>) detailsResponse.get("items");
                    if (!detailsItems.isEmpty()) {
                        Map<String, Object> contentDetails = (Map<String, Object>) detailsItems.get(0).get("contentDetails");
                        duration = (String) contentDetails.get("duration");
                    }
                }

                // 재생시간 변환 (ISO 8601 Duration 형식 -> 초 단위 변환)
                Integer ytbDur = parseYouTubeDuration(duration);

                Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
                Map<String, Object> thumbnails = (Map<String, Object>) snippet.get("thumbnails");
                Map<String, String> defaultThumbnail = (Map<String, String>) thumbnails.get("default");

                videoData.put("title", snippet.get("title"));
                videoData.put("contentUrl", "https://www.youtube.com/watch?v=" + id.get("videoId"));
                videoData.put("imageUrl", defaultThumbnail.get("url"));
                videoData.put("isUploaded", false);
                videoData.put("ctype", CType.YOUTUBE);
                videoData.put("ytbDur", ytbDur);

                String uuid = UUID.randomUUID().toString();
                videoData.put("itemId", uuid);

                System.out.println("Generated video data: " + videoData);

                crawledItemCache.put(uuid, videoData); // 캐시에 저장

                results.add(videoData);
            }
        }

        return results;
    }

    private Integer parseYouTubeDuration(String duration) {
        if (duration == null || duration.isEmpty()) {
            return 0; // null 또는 빈 값일 경우 기본값 반환
        }

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        try {
            // "PT" 제거
            duration = duration.replace("PT", "");

            // 시간 처리
            if (duration.contains("H")) {
                String[] split = duration.split("H");
                hours = Integer.parseInt(split[0]);
                duration = split.length > 1 ? split[1] : ""; // 남은 부분 업데이트
            }

            // 분 처리
            if (duration.contains("M")) {
                String[] split = duration.split("M");
                minutes = Integer.parseInt(split[0]);
                duration = split.length > 1 ? split[1] : ""; // 남은 부분 업데이트
            }

            // 초 처리
            if (duration.contains("S")) {
                seconds = Integer.parseInt(duration.replace("S", ""));
            }
        } catch (NumberFormatException e) {
            // 예상치 못한 형식일 경우 기본값 반환
            System.err.println("Invalid duration format: " + duration);
            return 0;
        }

        return hours * 3600 + minutes * 60 + seconds;
    }

    private String getSpotifyAccessToken() {
        String tokenUrl = "https://accounts.spotify.com/api/token";

        Map<String, Object> response = webClient.post()
                .uri(tokenUrl)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return (String) response.get("access_token");
    }

    public List<Map<String, Object>> searchSpotify(String query) {
        String url = String.format("%s/api/proxy/search?service=spotify&query=%s", proxyUrl, query);
        System.out.println("Proxy URL (Spotify): " + url); // 요청 URL 로그 추가

        Map<String, Object> response;
        try {
            response = webClient.get().uri(url).retrieve().bodyToMono(Map.class).block();
            System.out.println("Response from Proxy (Spotify): " + response); // 응답 로그 추가
        } catch (Exception e) {
            System.err.println("Error during Proxy request (Spotify): " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // 에러 발생 시 빈 리스트 반환
        }

        List<Map<String, Object>> results = new ArrayList<>();
        if (response != null && response.containsKey("tracks")) {
            Map<String, Object> tracks = (Map<String, Object>) response.get("tracks");
            List<Map<String, Object>> items = (List<Map<String, Object>>) tracks.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> trackData = new HashMap<>();
                Map<String, Object> album = (Map<String, Object>) item.get("album");
                List<Map<String, String>> artists = (List<Map<String, String>>) item.get("artists");
                Map<String, String> externalUrls = (Map<String, String>) item.get("external_urls");

                trackData.put("title", item.get("name"));
                trackData.put("contentUrl", externalUrls.get("spotify"));
                trackData.put("artist", artists.get(0).get("name"));
                trackData.put("imageUrl", ((Map<String, Object>) ((List<?>) album.get("images")).get(0)).get("url"));
                trackData.put("isUploaded", false);
                trackData.put("ctype", CType.MUSIC);

                String uuid = UUID.randomUUID().toString();
                trackData.put("itemId", uuid);

                crawledItemCache.put(uuid, trackData); // 캐시에 저장

                results.add(trackData);
            }
        }

        return results;
    }

    public List<Map<String, Object>> searchSpaces(String query) {
        List<Space> spaces = spaceRepository.findAll();
        List<Map<String, Object>> results = new ArrayList<>();

        for (Space space : spaces) {
            if (space.getSname() != null && space.getSname().contains(query)) {
                Map<String, Object> spaceData = new HashMap<>();
                spaceData.put("spaceId", space.getSpaceId());
                spaceData.put("sname", space.getSname());
                spaceData.put("userId", space.getUser().getUserId());
                spaceData.put("isPublic", space.getIsPublic());
                spaceData.put("createdAt", space.getCreatedAt());
                spaceData.put("ctype", CType.SPACE);

                results.add(spaceData);
            }
        }

        return results;
    }
}
