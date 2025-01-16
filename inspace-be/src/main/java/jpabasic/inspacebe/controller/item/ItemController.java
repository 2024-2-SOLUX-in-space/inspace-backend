package jpabasic.inspacebe.controller.item;

import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.service.item.ItemService;
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


    //아이템 페이지(아카이브)에 등록
    @PutMapping("/archive/{pageId}")
    public ResponseEntity<?> archiveItems(@PathVariable Integer pageId, @RequestBody List<ArchiveRequestDto> archiveDtos) {
        try{
            itemService.archiveItems(pageId,archiveDtos);
        }catch(Exception e){
            String message="아이템 등록에 실패했어요. 다시 시도해주세요.";
            return ResponseEntity.badRequest().body(message);
        }
        String message="페이지가 성공적으로 저장되었어요.";
        return ResponseEntity.ok().body(message);
    }

    //아이템 페이지에서 삭제 //put mapping //매핑된 page entity와의 관계 삭제하는 방향으로

    //아이템 저장소에서 삭제 //delete mapping

    //유저가 직접 올리는 이미지 저장 //POST

    //저장소 조회(카테고리별 아이템 전체 조회)

    //

}

