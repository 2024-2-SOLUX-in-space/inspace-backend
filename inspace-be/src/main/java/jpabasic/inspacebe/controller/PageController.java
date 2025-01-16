package jpabasic.inspacebe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jpabasic.inspacebe.converter.ResponseMessage;
import jpabasic.inspacebe.dto.SpaceDto;
import jpabasic.inspacebe.dto.page.PageDto;
import jpabasic.inspacebe.service.PageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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








}
