package jpabasic.inspacebe.controller.item;

import jpabasic.inspacebe.dto.item.ConfirmItemRequest;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.dto.item.TemporaryItemDto;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.service.item.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 검색 결과 반환
    @GetMapping("/search")
    public List<TemporaryItemDto> searchItems(@RequestParam String query) {
        return itemService.searchItems(query); // 크롤링 후 임시 데이터 반환
    }

    // 상세 보기
    @GetMapping("/temp/{uuid}")
    public ResponseEntity<TemporaryItemDto> getTemporaryItem(@PathVariable String uuid) {
        TemporaryItemDto item = itemService.getTemporaryItem(uuid);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }

    // 확정된 아이템 저장 (추가 버튼 클릭 시)
    @PostMapping("/add")
    public ResponseEntity<Item> addItem(@RequestBody ConfirmItemRequest request) {
        Item savedItem = itemService.addItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }
}

