package jpabasic.inspacebe.controller;

import jpabasic.inspacebe.dto.UpdateUserRequest;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-info")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 내 정보 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal String email) {
        Map<String, Object> response = new HashMap<>();

        System.out.println("Authenticated Email: " + email);

        // 사용자 조회
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            response.put("success", false);
            response.put("message", "인증이 실패하였습니다. 유효하지 않은 사용자입니다.");
            return ResponseEntity.status(401).body(response);
        }

        // 사용자 정보를 반환
        response.put("success", true);
        response.put("message", "사용자 정보를 조회했습니다.");

        // 사용자 정보 포함
        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getName()); // 수정 가능 기본값
        data.put("email", user.getEmail()); // 읽기 전용
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 내 정보 수정
    @PatchMapping
    public ResponseEntity<Map<String, Object>> updateUserInfo(@AuthenticationPrincipal String email,
                                                              @RequestBody UpdateUserRequest request) {
        Map<String, Object> response = new HashMap<>();

        System.out.println("Authenticated Email: " + email);

        // 사용자 조회
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            response.put("success", false);
            response.put("message", "인증이 실패하였습니다. 유효하지 않은 사용자입니다.");
            return ResponseEntity.status(401).body(response);
        }

        // 비밀번호 확인 일치 여부 확인
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            response.put("success", false);
            response.put("message", "비밀번호 확인이 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        // 사용자 정보 업데이트 (이메일 수정 방지)
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "회원 정보가 성공적으로 수정되었습니다.");
        return ResponseEntity.ok(response);
    }
}
