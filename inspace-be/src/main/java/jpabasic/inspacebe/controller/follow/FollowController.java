package jpabasic.inspacebe.controller.follow;

import jpabasic.inspacebe.config.CurrentUser;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping(value = "/new/{followedId}")
    public ResponseEntity<String> followUser(@PathVariable Integer followedId, @CurrentUser User following){
        String response = followService.follow(followedId, following);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value="/undo/{followedId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Integer followedId, @CurrentUser User following){
        String response = followService.unfollow(followedId, following);
        return ResponseEntity.ok(response);
    }
}
