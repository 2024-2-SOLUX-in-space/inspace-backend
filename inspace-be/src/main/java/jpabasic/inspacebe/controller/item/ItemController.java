package jpabasic.inspacebe.controller.item;

import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.service.item.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 아이템 등록 (랜덤 ID 생성)
    @PostMapping("/register")
    public ResponseEntity<Item> registerItem(@RequestBody ItemRequestDto itemRequestDto) {
        Item item = itemService.registerItem(itemRequestDto);
        return ResponseEntity.ok(item);
    }

    // 아이템 상세 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemDetails(@PathVariable String itemId) {
        ItemResponseDto itemResponseDto = itemService.getItemDetails(itemId);
        return ResponseEntity.ok(itemResponseDto);
    }
}

