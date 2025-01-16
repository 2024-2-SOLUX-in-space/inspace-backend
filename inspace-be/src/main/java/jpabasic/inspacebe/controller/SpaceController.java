package jpabasic.inspacebe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jpabasic.inspacebe.converter.ResponseMessage;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.service.SpaceService;
import jpabasic.inspacebe.dto.SpaceDto;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name="공간 설정 관련 api들")
public class SpaceController {

    private final SpaceService spaceService;


    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }


    @PostMapping("/spaces/{user_id}")
    @Operation(summary="공간 신규 등록")
    public ResponseEntity<?> createSpace(@Valid @RequestBody SpaceDto spaceDto, @PathVariable Integer user_id) {
        SpaceDto dto;
        try {
            dto = spaceService.createSpace(spaceDto, user_id);
        } catch (Exception e) {
            String message = "공간 등록에 실패했어요. 다시 시도해주세요.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/spaces/{user_id}")
    @Operation(summary="공간 목록 조회")
    public ResponseEntity<?> getSpaces(@PathVariable Integer user_id) {
        List<SpaceDto> spaces;
        try {
            spaces = spaceService.getSpaces(user_id);
        } catch (Exception e) {
            String message = "해당 사용자의 공간 목록을 찾을 수 없습니다.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }
        return new ResponseEntity<>(spaces, HttpStatus.OK);
    }

    //예외처리해야.
    //우선 대표 공간만 보이도록?
    @GetMapping("/space/{space_id}")
    @Operation(summary="특정 공간 조회")
    public ResponseEntity<?> getSpace(@PathVariable Integer space_id){
        SpaceDto space=spaceService.getSpace(space_id);

        if(space==null){
            String message="해당 공간을 찾을 수 없어요.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }
        return new ResponseEntity<>(space, HttpStatus.OK);
    }

    @PutMapping("/spaces/{space_id}") //user_id통해 본인만 설정 변경할 수 있도록.
    @Operation(summary="공간 설정 변경")
    public ResponseEntity<?> updateSpace(@PathVariable Integer space_id,@Valid @RequestBody SpaceDto spaceDto) {
        SpaceDto space;
        try {
            space = spaceService.updateSpace(space_id, spaceDto).getBody();
        } catch (Exception e) {
            String message = "공간 수정에 실패했어요.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(space);
    }

    @DeleteMapping("/spaces/{space_id}/{user_id}")
    @Operation(summary="공간 삭제")
    public ResponseEntity<?> deleteSpace(@PathVariable int space_id){
        try{
            spaceService.deleteSpace(space_id);
        }catch(Exception e){
            String message = "공간 삭제에 실패했어요.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }
        String message="공간이 성공적으로 삭제되었습니다.";
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }


}