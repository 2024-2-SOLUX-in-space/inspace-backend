package jpabasic.inspacebe.service.follow;

import jpabasic.inspacebe.dto.follow.FollowResponseDto;
import jpabasic.inspacebe.entity.Follow;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.FollowRepository;
import jpabasic.inspacebe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

    // "내가 팔로우하는" 사용자 목록
    public List<FollowResponseDto> getFollowings(User following) {
        List<Follow> followings = followRepository.findAllByFollowing(following);

        return followings.stream()
                .map(follow -> {
                    return FollowResponseDto.fromEntity(follow.getFollowed());
                })
                .collect(Collectors.toList());
    }

    // "나를 팔로우하는" 사용자 목록
    public List<FollowResponseDto> getFollowers(User followed) {
        List<Follow> followers = followRepository.findAllByFollowed(followed);

        return followers.stream()
                .map(follow -> {
                    return FollowResponseDto.fromEntity(follow.getFollowing());
                })
                .collect(Collectors.toList());
    }

}