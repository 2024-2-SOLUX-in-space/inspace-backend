package jpabasic.inspacebe.controller.search;

import jpabasic.inspacebe.dto.search.SearchResponseDto;
import jpabasic.inspacebe.service.search.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jpabasic.inspacebe.service.search.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @Operation
    @GetMapping("/results")
    public Map<String, Object> search(
            @Parameter(name = "query", description = "검색어", required = true) @RequestParam("query") String query,
            @Parameter(name = "filter", description = "필터 (image, youtube, music, space)") @RequestParam(value = "filter", required = false) List<String> filters) {

        Map<String, Object> results = searchService.searchAll(query, filters);

        return results;
    }
}
