package jpabasic.inspacebe.controller.search;

import jpabasic.inspacebe.dto.search.SearchResponseDto;
import jpabasic.inspacebe.service.search.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SearchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    public SearchControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
    }

    @Test
    void testSearchImagesAndVideos() throws Exception {
        // Mocking 서비스 레이어의 결과값
        List<Map<String, String>> mockImageResults = Collections.emptyList(); // 반환 타입에 맞게 수정
        List<Map<String, String>> mockVideoResults = Collections.emptyList();
        List<Map<String, String>> mockMusicResults = Collections.emptyList();

        when(searchService.searchImages("testQuery")).thenReturn(mockImageResults);
        when(searchService.searchYouTube("testQuery")).thenReturn(mockVideoResults);
        when(searchService.searchSpotify("testQuery")).thenReturn(mockMusicResults);

        mockMvc.perform(get("/api/search/results")
                        .param("query", "testQuery")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.images").exists())
                .andExpect(jsonPath("$.videos").exists())
                .andExpect(jsonPath("$.music").exists());
    }
}
