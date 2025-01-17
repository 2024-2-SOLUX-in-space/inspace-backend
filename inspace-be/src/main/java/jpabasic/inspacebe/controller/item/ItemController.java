package jpabasic.inspacebe.controller.item;

import io.swagger.v3.oas.annotations.Operation;
import jpabasic.inspacebe.converter.ResponseMessage;
import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.item.ItemRequestDto;

import jpabasic.inspacebe.dto.SpaceDetailResponseDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.dto.item.UserImageDto;
import jpabasic.inspacebe.service.SpaceService;
import jpabasic.inspacebe.service.item.ItemService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;
    private final SpaceService spaceService;

    public ItemController(ItemService itemService, SpaceService spaceService) {
        this.itemService = itemService;
        this.spaceService = spaceService;
    }


    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemDetails(@PathVariable("itemId") String itemId) {
        ItemResponseDto response = itemService.getItemDetails(itemId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/items/space/{spaceId}")
    public ResponseEntity<SpaceDetailResponseDto> getSpaceDetails(@PathVariable("spaceId") int spaceId) {
        SpaceDetailResponseDto response = itemService.getSpaceDetails(spaceId);
        return ResponseEntity.ok(response);
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
    //유저가 정하는 제목과 file -> dto로

    @PostMapping(value = "/image", consumes = "multipart/form-data", produces = "application/json")
    @Operation(summary = "유저가 직접 올리는 이미지 저장")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("spaceId") Integer spaceId,
                                                           @RequestParam("title") String title) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                response.put("error", "파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 파일 업로드 및 DB 저장 처리
            String fileUrl = itemService.uploadImageAndSaveTodb(file, spaceId, title);

            // 성공 응답
            response.put("message", "이미지를 성공적으로 업로드했어요.");
            response.put("fileUrl", fileUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "알 수 없는 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }





    //저장소 조회(카테고리별 아이템 전체 조회) //아직 카테고리별 처리 안함.
    @GetMapping("/cateegory/space/{spaceId}")
    @Operation(summary="저장소 조회(카테고리별 아이템 전체 조회)")
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

