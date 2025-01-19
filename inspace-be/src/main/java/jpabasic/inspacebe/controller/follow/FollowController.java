package jpabasic.inspacebe.controller.follow;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jpabasic.inspacebe.config.CurrentUser;
import jpabasic.inspacebe.dto.follow.FollowResponseDto;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@Tag(name="팔로우 관련 API")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "팔로우하기")
    @PostMapping(value = "/new/{followedId}")
    public ResponseEntity<String> followUser(
            @Parameter(name = "followedId", description = "팔로우하려는 사람의 ID", required = true, in = ParameterIn.PATH)
            @PathVariable("followedId") Integer followedId, @CurrentUser User following) {
        String response = followService.follow(followedId, following);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "팔로우 취소하기")
    @DeleteMapping(value="/undo/{followedId}")
    public ResponseEntity<String> unfollowUser(@Parameter(name = "followedId", description = "팔로우 취소하려는 사람의 ID", required = true, in = ParameterIn.PATH) @PathVariable("followedId") Integer followedId, @CurrentUser User following){
        String response = followService.unfollow(followedId, following);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 팔로잉하는 사용자 목록 보기")
    @GetMapping(value = "/followings")
    public ResponseEntity<List<FollowResponseDto>> getFollowings(@CurrentUser User following){
        return ResponseEntity.ok(followService.getFollowings(following));
    }

    @Operation(summary = "나를 팔로우하는 사용자 목록 보기")
    @GetMapping(value = "/followers")
    public ResponseEntity<List<FollowResponseDto>> getFollowers(@CurrentUser User followed){
        return ResponseEntity.ok(followService.getFollowers(followed));
    }
}
