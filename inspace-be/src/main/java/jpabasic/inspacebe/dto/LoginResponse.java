package jpabasic.inspacebe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String email;         // 사용자 이메일
    private String accessToken;   // 액세스 토큰
    private String refreshToken;  // 리프레시 토큰
}
