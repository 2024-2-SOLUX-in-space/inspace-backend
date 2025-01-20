package jpabasic.inspacebe.dto.follow;

import jpabasic.inspacebe.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowResponseDto {
    public Integer followingId;
    public String name;

    public  static FollowResponseDto fromEntity(User followingUser) {
        return new FollowResponseDto(followingUser.getUserId(), followingUser.getName());
    }
}
