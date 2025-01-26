package jpabasic.inspacebe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jpabasic.inspacebe.converter.ResponseMessage;
import jpabasic.inspacebe.dto.SpaceDto;
import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.page.PageDto;
import jpabasic.inspacebe.service.PageService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/page")
@Tag(name="페이지 관련 api들")
public class PageController {
    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }


    //페이지 조회 //완료
  @GetMapping("")
    @Operation(summary="페이지 조회")
    public ResponseEntity<?> getPage(@RequestParam("space_id") Integer space_id,@RequestParam("pageNum") int pageNum) {
        try{
            List<ArchiveRequestDto> items=pageService.getPage(pageNum,space_id);

            //빈 배열도 정상 응답으로 처리
            return ResponseEntity.ok(items);
        }catch(IllegalArgumentException e){

            // 요청 데이터가 잘못된 경우 (404 처리)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("페이지 조회에 실패했습니다."));
        }
    }


    //아이템 페이지(아카이브)에 등록 //pageId 할당-등록 시
    @PutMapping("/{pageId}")
    @Operation(summary="페이지(아카이브)에 아이템 등록/수정")
    public ResponseEntity<?> archiveItems(@PathVariable Integer pageId, @RequestBody List<ArchiveRequestDto> archiveDtos) {
        try{
            pageService.archiveSticker(archiveDtos);

            pageService.archiveItems(pageId,archiveDtos);
        }catch(Exception e){
            String message="아이템 등록에 실패했어요. 다시 시도해주세요.";
            return ResponseEntity.badRequest().body(message);
        }
        String message="페이지가 성공적으로 저장되었어요.";
        return ResponseEntity.ok().body(message);
    }

    //아이템 페잊이(아카이브)에 스티커 등록 시
    @PostMapping("/sticker/{pageId}")
    @Operation(summary="페이지(아카이브)에 스티커 등록")
    public ResponseEntity<?> archiveSticker(@RequestBody List<ArchiveRequestDto> archiveDtos) {
        try{
            pageService.archiveSticker(archiveDtos);
        }catch(Exception e){
            String messsage="스티커 등록에 실패했어요.";
            return ResponseEntity.badRequest().body(messsage);
        }
        String message="스티커가 성공적으로 저장되었어요.";
        return ResponseEntity.ok().body(message);
    }



    //페이지(아카이브)에서 아이템 삭제  //완료
    @DeleteMapping("/{itemId}")
    @Operation(summary="페이지(아카이브)에서 아이템 삭제")
    public ResponseEntity<?> deleteItemOnPage(@PathVariable("itemId") String itemId) {
        try{
            pageService.deleteItemOnPage(itemId);
        }catch(Exception e){
            String message="페이지에서 해당 아이템을 삭제하는데 실패했어요.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
        String message="페이지에서 해당 아이템을 성공적으로 삭제했어요.";
        return new ResponseEntity<>(message,HttpStatus.OK);
    }









}
