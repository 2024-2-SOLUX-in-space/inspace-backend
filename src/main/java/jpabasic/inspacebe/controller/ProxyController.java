package jpabasic.inspacebe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import se.michaelthelin.spotify.SpotifyApi;

import java.util.*;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    private final RestTemplate restTemplate;
    private final SpotifyApi spotifyApi;
    private final String spotifyAccessToken;

    @Value("${google.api-key}")
    private String googleApiKey;

    @Value("${google.search-engine-id}")
    private String googleSearchEngineId;

    @Value("${youtube.api-key}")
    private String youtubeApiKey;


    public ProxyController(RestTemplate restTemplate, SpotifyApi spotifyApi, String spotifyAccessToken) {
        this.restTemplate = restTemplate;
        this.spotifyApi = spotifyApi;
        this.spotifyAccessToken = spotifyAccessToken;
    }

    @GetMapping("/search")
    public ResponseEntity<?> proxyGet(@RequestParam("service") String services, @RequestParam Map<String, String> queryParams) {
        List<String> urls = getTargetUrls(services, queryParams);
        Map<String, Object> combinedResults = new HashMap<>();

        for (String url : urls) {
            System.out.println("[soyg] Sending request to URL: " + url);
            System.out.println("Request Headers: Authorization=" + queryParams.get("Authorization")); // 요청 헤더 로그
            ResponseEntity<Map> response;
            try {
                response = restTemplate.getForEntity(url, Map.class);
                System.out.println("[soyg] Proxy Response: " + response.getBody());
                System.out.println("[soyg] Response from proxy: " + response.getStatusCode()); // 응답 상태 확인
                if (response.getBody() != null) {
                    combinedResults.putAll(response.getBody());
                }
            } catch (Exception e) {
                System.err.println("[soyg] Error while calling proxy URL: " + url);
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Proxy server error: " + e.getMessage());
            }
        }

        // CORS 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "http://localhost:3000"); // 특정 Origin만 허용
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS"); // 허용할 메서드 추가
        headers.add("Access-Control-Allow-Headers", "*"); // 모든 헤더 허용
        headers.add("Access-Control-Allow-Credentials", "true"); // 쿠키 및 인증 정보 허용
        return ResponseEntity.ok(combinedResults);
    }

    private List<String> getTargetUrls(String services, Map<String, String> queryParams) {
        List<String> urls = new ArrayList<>();
        String[] serviceArray = services.split(",");

        for (String service : serviceArray) {
            String baseUrl;
            switch (service.trim()) {
                case "image":
                    baseUrl = "https://www.googleapis.com/customsearch/v1";
                    queryParams.put("cx", googleSearchEngineId);
                    queryParams.put("key", googleApiKey);
                    queryParams.put("searchType", "image");
                    queryParams.put("q", queryParams.get("query"));
                    break;
                case "youtube":
                    baseUrl = "https://www.googleapis.com/youtube/v3/search";
                    queryParams.put("part", "snippet");
                    queryParams.put("maxResults", "10");
                    queryParams.put("key", youtubeApiKey);
                    queryParams.put("q", queryParams.get("query"));
                    break;
                case "music":
                    baseUrl = "https://api.spotify.com/v1/search";
                    queryParams.put("type", "track");
                    queryParams.put("limit", "5");
                    queryParams.put("q", queryParams.get("query"));

                    queryParams.put("Authorization", "Bearer " + spotifyAccessToken);
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