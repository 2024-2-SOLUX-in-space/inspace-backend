package jpabasic.inspacebe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    private final RestTemplate restTemplate;

    @Value("${google.api-key}")
    private String googleApiKey;

    @Value("${google.search-engine-id}")
    private String googleSearchEngineId;

    @Value("${youtube.api-key}")
    private String youtubeApiKey;

    @Value("${spotify.client-id}")
    private String spotifyClientId;

    @Value("${spotify.client-secret}")
    private String spotifyClientSecret;

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/search")
    public ResponseEntity<?> proxyGet(@RequestParam("service") String services, @RequestParam Map<String, String> queryParams) {
        List<String> urls = getTargetUrls(services, queryParams);
        Map<String, Object> combinedResults = new HashMap<>();

        for (String url : urls) {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getBody() != null) {
                combinedResults.putAll(response.getBody());
            }
        }
        return ResponseEntity.ok(combinedResults);
    }

    private List<String> getTargetUrls(String services, Map<String, String> queryParams) {
        List<String> urls = new ArrayList<>();
        String[] serviceArray = services.split(",");

        for (String service : serviceArray) {
            String baseUrl;
            switch (service.trim()) {
                case "image":  // 이미지 검색 (image)
                    baseUrl = "https://www.googleapis.com/customsearch/v1";
                    queryParams.put("cx", googleSearchEngineId);
                    queryParams.put("key", googleApiKey);
                    queryParams.put("searchType", "image");
                    break;
                case "youtube": // 유튜브 검색
                    baseUrl = "https://www.googleapis.com/youtube/v3/search";
                    queryParams.put("part", "snippet");
                    queryParams.put("maxResults", "10");
                    queryParams.put("key", youtubeApiKey);
                    break;
                case "music":  // Spotify 검색
                    baseUrl = "https://api.spotify.com/v1/search";
                    queryParams.put("type", "track");
                    queryParams.put("limit", "5");
                    break;
                default:
                    continue;
            }
            StringBuilder url = new StringBuilder(baseUrl + "?");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                url.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            urls.add(url.toString());
        }
        return urls;
    }
}