package jpabasic.inspacebe.controller.item;

import io.swagger.v3.oas.annotations.Operation;
import jpabasic.inspacebe.converter.ResponseMessage;
import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
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


    //아이템 저장소에서 삭제 //자식 객체는 삭제되면 안됨. (youtube,image..)
    @DeleteMapping("/{itemId}")
    @Operation(summary="아이템을 저장소에서 삭제")
    public ResponseEntity<?> deleteItemOnSpace(@PathVariable String itemId) {
        try{
            itemService.deleteItemOnSpace(itemId);
        }catch(Exception e){
            String message="해당 아이템을 삭제하는데 실패했어요.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
        String message="해당 아이템을 성공적으로 삭제했어요.";
        return new ResponseEntity<>(message,HttpStatus.OK);
    }



    //유저가 직접 올리는 이미지 저장 //POST



    //저장소 조회(카테고리별 아이템 전체 조회) //아직 카테고리별 처리 안함.
    @GetMapping("/space/{spaceId}")
    public ResponseEntity<?> getItemsBySpace(@PathVariable Integer spaceId) {
        ResponseEntity<List<ItemResponseDto>> items;
        try {
            items = itemService.getItemsBySpace(spaceId);
        } catch (Exception e) {
            String message = "아이템 조회에 실패했어요.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
        return items;
    }


}

