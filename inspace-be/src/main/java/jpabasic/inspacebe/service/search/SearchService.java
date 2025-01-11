package jpabasic.inspacebe.service.search;

import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class SearchService {

    private final ItemRepository itemRepository;
    private final WebClient webClient;
    private final Map<String, Map<String, Object>> crawledItemCache = new HashMap<>();

    public Optional<Map<String, Object>> getCachedItem(String itemId) {
        return Optional.ofNullable(crawledItemCache.get(itemId));
    }

    public void saveToCache(String itemId, Map<String, Object> itemData) {
        crawledItemCache.put(itemId, itemData);
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

    public SearchService(ItemRepository itemRepository, WebClient.Builder webClientBuilder) {
        this.itemRepository = itemRepository;
        this.webClient = webClientBuilder.build();
    }

    public Map<String, Object> searchAll(String query, List<String> filters) {
        Map<String, Object> results = new HashMap<>();

        if (filters == null || filters.isEmpty()) {
            results.put("images", searchImages(query));
            results.put("uploadedImages", searchUserUploadedImages(query));
            results.put("videos", searchYouTube(query));
            results.put("music", searchSpotify(query));
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
            results.put("space", Collections.emptyList());
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
            results.add(imageData);
        }

        return results;
    }

    public List<Map<String, Object>> searchImages(String query) {
        String url = String.format(
                "https://www.googleapis.com/customsearch/v1?q=%s&cx=%s&key=%s&searchType=image",
                query, searchEngineId, apiKey
        );

        Map<String, Object> response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> results = new ArrayList<>();
        if (response != null && response.containsKey("items")) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> imageData = new HashMap<>();
                imageData.put("title", item.get("title"));
                Map<String, String> image = (Map<String, String>) item.get("image");
                imageData.put("url", image.get("thumbnailLink"));
                imageData.put("contextLink", item.get("link"));
                imageData.put("isUploaded", false);

                String uuid = UUID.randomUUID().toString();
                imageData.put("itemId", uuid);
                crawledItemCache.put(uuid, imageData); // 캐시에 저장

                results.add(imageData);
            }
        }

        return results;
    }

    public List<Map<String, Object>> searchYouTube(String query) {
        String url = String.format(
                "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s&type=video&maxResults=10&key=%s",
                query, youtubeApiKey
        );

        Map<String, Object> response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> results = new ArrayList<>();
        if (response != null && response.containsKey("items")) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> videoData = new HashMap<>();
                Map<String, Object> id = (Map<String, Object>) item.get("id");
                Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
                Map<String, Object> thumbnails = (Map<String, Object>) snippet.get("thumbnails");
                Map<String, String> defaultThumbnail = (Map<String, String>) thumbnails.get("default");

                videoData.put("title", snippet.get("title"));
                videoData.put("url", "https://www.youtube.com/watch?v=" + id.get("videoId"));
                videoData.put("thumbnail", defaultThumbnail.get("url"));
                videoData.put("isUploaded", false);

                String uuid = UUID.randomUUID().toString();
                videoData.put("itemId", uuid);
                crawledItemCache.put(uuid, videoData); // 캐시에 저장

                results.add(videoData);
            }
        }

        return results;
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
        String accessToken = getSpotifyAccessToken();
        String url = String.format("https://api.spotify.com/v1/search?q=%s&type=track&limit=5", query);

        Map<String, Object> response = webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

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
                trackData.put("url", externalUrls.get("spotify"));
                trackData.put("artist", artists.get(0).get("name"));
                trackData.put("thumbnail", ((Map<String, Object>) ((List<?>) album.get("images")).get(0)).get("url"));
                trackData.put("isUploaded", false);

                String uuid = UUID.randomUUID().toString();
                trackData.put("itemId", uuid);
                crawledItemCache.put(uuid, trackData); // 캐시에 저장

                results.add(trackData);
            }
        }

        return results;
    }
}
