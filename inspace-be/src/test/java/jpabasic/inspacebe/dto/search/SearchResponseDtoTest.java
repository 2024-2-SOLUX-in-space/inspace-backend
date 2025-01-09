package jpabasic.inspacebe.dto.search;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SearchResponseDtoTest {

    @Test
    void testSearchResponseDto() {
        SearchResponseDto dto = new SearchResponseDto(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        assertNotNull(dto);
        assertEquals(0, dto.getImages().size());
        assertEquals(0, dto.getVideos().size());
        assertEquals(0, dto.getMusic().size());
    }
}
