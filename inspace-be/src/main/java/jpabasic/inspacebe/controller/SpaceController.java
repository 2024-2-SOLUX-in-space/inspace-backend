package jpabasic.inspacebe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.service.SpaceService;
import jpabasic.inspacebe.dto.SpaceDto;
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
    public ResponseEntity<SpaceDto> createSpace(@Valid @RequestBody SpaceDto spaceDto, @PathVariable Integer user_id){
        spaceService.createSpace(spaceDto,user_id);
        return new ResponseEntity<>(spaceDto, HttpStatus.CREATED);
    }

    @GetMapping("/spaces/{user_id}")
    @Operation(summary="공간 목록 조회")
    public ResponseEntity<List<SpaceDto>> getSpaces(@PathVariable Integer user_id){
        List<SpaceDto> spaces=spaceService.getSpaces(user_id);
        return new ResponseEntity<>(spaces, HttpStatus.OK);
    }

    //예외처리해야.
    @GetMapping("/space/{space_id}")
    @Operation(summary="특정 공간 조회")
    public ResponseEntity<SpaceDto> getSpace(@PathVariable Integer space_id){
        SpaceDto space=spaceService.getSpace(space_id);
        return new ResponseEntity<>(space, HttpStatus.OK);
    }

    @PutMapping("/spaces/{space_id}/{user_id}") //user_id통해 본인만 설정 변경할 수 있도록.
    @Operation(summary="공간 설정 변경")
    public ResponseEntity<SpaceDto> updateSpace(@PathVariable Integer space_id,@PathVariable Integer user_id, @Valid @RequestBody SpaceDto spaceDto){
        SpaceDto space= spaceService.updateSpace(space_id,spaceDto).getBody();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(space);
    }

    @DeleteMapping("/spaces/{space_id}/{user_id}") //user_id는 본인 확인 위해?
    @Operation(summary="공간 삭제")
    public ResponseEntity<String> deleteSpace(@PathVariable int space_id){
        spaceService.deleteSpace(space_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}