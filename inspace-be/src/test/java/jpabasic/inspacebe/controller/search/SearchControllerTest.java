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
    void testSearchResults() throws Exception {
        // Mocking SearchService의 searchAll 결과값
        Map<String, Object> mockSearchResults = new HashMap<>();
        mockSearchResults.put("images", List.of(Collections.singletonMap("title", "Image 1")));
        mockSearchResults.put("videos", List.of(Collections.singletonMap("title", "Video 1")));
        mockSearchResults.put("music", List.of(Collections.singletonMap("title", "Music 1")));

        when(searchService.searchAll("testQuery", null)).thenReturn(mockSearchResults);

        // API 호출 및 검증
        mockMvc.perform(get("/api/search/results")
                        .param("query", "testQuery")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.images").exists())
                .andExpect(jsonPath("$.videos").exists())
                .andExpect(jsonPath("$.music").exists())
                .andExpect(jsonPath("$.images[0].title").value("Image 1"))
                .andExpect(jsonPath("$.videos[0].title").value("Video 1"))
                .andExpect(jsonPath("$.music[0].title").value("Music 1"));
    }
}