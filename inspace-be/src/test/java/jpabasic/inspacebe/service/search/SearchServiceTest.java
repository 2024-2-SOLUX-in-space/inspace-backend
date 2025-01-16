package jpabasic.inspacebe.service.search;

import jpabasic.inspacebe.dto.search.SearchResponseDto;
import jpabasic.inspacebe.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ItemRepository itemRepository;

    @Mock
    private RestTemplate restTemplate;

    public SearchServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchImages() {
        String query = "testQuery";
        String mockUrl = "https://www.googleapis.com/customsearch/v1?q=testQuery&cx=null&key=null&searchType=image";

        // Mocking RestTemplate 호출 결과
        List<Map<String, Object>> mockResponse = Collections.emptyList(); // 반환 타입에 맞게 수정
        when(searchService.searchImages(query)).thenReturn(mockResponse);

        List<Map<String, Object>> result = searchService.searchImages(query);

        assertNotNull(result);
    }

    @Test
    void testSearchYouTube() {
        String query = "testQuery";
        String mockUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=testQuery&type=video&maxResults=10&key=null";

        Map<String, Object> mockApiResponse = Map.of("items", Collections.emptyList());
        when(restTemplate.getForEntity(mockUrl, Map.class)).thenReturn(ResponseEntity.ok(mockApiResponse));

        var result = searchService.searchYouTube(query);

        assertNotNull(result);
    }

    @Test
    void testSearchSpotify() {
        // Mocking Spotify API 토큰 요청 및 검색 요청
        when(restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                null,
                null,
                Map.class
        )).thenReturn(ResponseEntity.ok(Map.of("access_token", "mockToken")));

        when(restTemplate.exchange(
                "https://api.spotify.com/v1/search?q=testQuery&type=track&limit=5",
                null,
                null,
                Map.class
        )).thenReturn(ResponseEntity.ok(Map.of("tracks", Map.of("items", Collections.emptyList()))));

        var result = searchService.searchSpotify("testQuery");

        assertNotNull(result);
    }
}
