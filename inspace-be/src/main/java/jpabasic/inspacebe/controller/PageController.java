package jpabasic.inspacebe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jpabasic.inspacebe.converter.ResponseMessage;
import jpabasic.inspacebe.dto.SpaceDto;
import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.page.PageDto;
import jpabasic.inspacebe.service.PageService;
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


    //페이지 조회
    @GetMapping("")
    @Operation(summary="페이지 조회")
    public ResponseEntity<?> getPage(@RequestParam Integer pageNum,@RequestParam Integer spaceId) {
        PageDto page;
        try{
            page=pageService.getPage(pageNum,spaceId);
        }catch(Exception e){
            String message="페이지 조회에 실패했어요.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
        return new ResponseEntity<>(page,HttpStatus.OK);
    }


    //아이템 페이지(아카이브)에 등록
    @PutMapping("/{pageId}")
    public ResponseEntity<?> archiveItems(@PathVariable Integer pageId, @RequestBody List<ArchiveRequestDto> archiveDtos) {
        try{
            pageService.archiveItems(pageId,archiveDtos);
        }catch(Exception e){
            String message="아이템 등록에 실패했어요. 다시 시도해주세요.";
            return ResponseEntity.badRequest().body(message);
        }
        String message="페이지가 성공적으로 저장되었어요.";
        return ResponseEntity.ok().body(message);
    }



    //페이지(아카이브)에서 아이템 삭제
    @DeleteMapping("/{itemId}")
    @Operation(summary="페이지(아카이브)에서 아이템 삭제")
    public ResponseEntity<?> deleteItemOnPage(@PathVariable String itemId) {
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
