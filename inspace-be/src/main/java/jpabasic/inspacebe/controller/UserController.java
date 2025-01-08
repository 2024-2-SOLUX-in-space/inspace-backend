package jpabasic.inspacebe.controller;

import jpabasic.inspacebe.dto.LoginRequest;
import jpabasic.inspacebe.dto.RegisterRequest;
import jpabasic.inspacebe.dto.LoginResponse;
import jpabasic.inspacebe.dto.ApiResponse;
import jpabasic.inspacebe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // 추가

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest request) { // @Valid 추가
        try {
            userService.registerUser(request); // 회원가입 로직 실행
            ApiResponse response = new ApiResponse(true, "회원가입이 완료되었습니다! 로그인을 진행해 주세요.");
            return ResponseEntity.ok(response); // 성공 응답
        } catch (IllegalArgumentException e) {
            ApiResponse errorResponse = new ApiResponse(false, e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse); // 입력값 오류 처리
        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse(false, "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(errorResponse); // 기타 오류 처리
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) { // @Valid 추가
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
