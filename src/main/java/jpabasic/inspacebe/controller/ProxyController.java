package jpabasic.inspacebe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;


@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    private final RestTemplate restTemplate;

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${google.api-key}")
    private String googleApiKey;

    @Value("${google.search-engine-id}")
    private String googleSearchEngineId;

    @Value("${youtube.api-key}")
    private String youtubeApiKey;

    @GetMapping("/search")
    public ResponseEntity<?> proxyGet(
            @RequestParam("query") String query,
            @RequestParam(value = "filter", required = false) String filter) {

        String targetUrl = getTargetUrl(filter, query);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, String.class);

        return ResponseEntity.ok(response.getBody());
    }

    private String getTargetUrl(String filter, String query) {
        String baseUrl;
        switch (filter) {
            case "image":
                baseUrl = "https://www.googleapis.com/customsearch/v1";
                return baseUrl + "?q=" + query + "&cx=" + googleSearchEngineId + "&key=" + googleApiKey + "&searchType=image";
            case "youtube":
                baseUrl = "https://www.googleapis.com/youtube/v3/search";
                return baseUrl + "?part=snippet&q=" + query + "&type=video&maxResults=10&key=" + youtubeApiKey;
            case "music":
                baseUrl = "https://api.spotify.com/v1/search";
                return baseUrl + "?q=" + query + "&type=track&limit=5";
            default:
                throw new IllegalArgumentException("지원하지 않는 필터입니다.");
        }
    }
}
