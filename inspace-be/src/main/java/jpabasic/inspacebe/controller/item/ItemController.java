package jpabasic.inspacebe.controller.item;


import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.service.item.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemDetails(@PathVariable("itemId") String itemId) {
        ItemResponseDto response = itemService.getItemDetails(itemId);
        return ResponseEntity.ok(response);
    }
}

