package jpabasic.inspacebe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.*;


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
            @RequestParam("service") String services,
            @RequestParam Map<String, String> queryParams) {

        List<String> urls = getTargetUrls(services, queryParams);
        Map<String, Object> combinedResults = new HashMap<>();

        for (String url : urls) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getBody() != null) {
                combinedResults.putAll(response.getBody());  // API 결과 합치기
            }
        }

        return ResponseEntity.ok(combinedResults);
    }

    private List<String> getTargetUrls(String services, Map<String, String> queryParams) {
        List<String> urls = new ArrayList<>();
        String[] serviceArray = services.split(",");

        for (String service : serviceArray) {
            String baseUrl;
            Map<String, String> params = new HashMap<>(queryParams);

            switch (service.trim()) {
                case "google":
                    baseUrl = "https://www.googleapis.com/customsearch/v1";
                    params.put("cx", googleSearchEngineId);
                    params.put("key", googleApiKey);
                    break;
                case "youtube":
                    baseUrl = "https://www.googleapis.com/youtube/v3/search";
                    params.put("part", "snippet");
                    params.put("maxResults", "10");
                    params.put("key", youtubeApiKey);
                    break;
                case "spotify":
                    baseUrl = "https://api.spotify.com/v1/search";
                    params.put("type", "track");
                    params.put("limit", "5");
                    break;
                default:
                    continue;
            }

            // URL 생성
            StringBuilder url = new StringBuilder(baseUrl + "?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }

            urls.add(url.toString());
        }
        return urls;
    }
}
