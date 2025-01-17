package jpabasic.inspacebe.service.follow;

import jpabasic.inspacebe.entity.Follow;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.FollowRepository;
import jpabasic.inspacebe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public String follow(Integer followedId, User following) {
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우할 사용자를 찾을 수 없습니다."));
        if (followRepository.existsByFollowingAndFollowed(following, followed)) {
            return "이미 팔로우 중입니다.";
        }
        followRepository.save(new Follow(followed, following));
        return followed.getName() + "님을 팔로우하였습니다.";
    }

    public String unfollow(Integer followedId, User following) {
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new IllegalArgumentException("언팔로우할 사용자를 찾을 수 없습니다."));
        if (!followRepository.existsByFollowingAndFollowed(following, followed)) {
            return "팔로우하고 있지 않아 언팔로우할 수 없습니다.";
        }
        followRepository.delete(new Follow(followed, following));
        return followed.getName() + "님을 언팔로우하였습니다.";
    }
}