package jpabasic.inspacebe.controller;

import jpabasic.inspacebe.dto.UpdateUserRequest;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // 사용자 정보 조회
    @GetMapping
    public Map<String, Object> getUserInfo() {
        Map<String, Object> response = new HashMap<>();

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // 이메일은 인증된 사용자 이름으로 사용

        // 사용자 조회
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            response.put("success", false);
            response.put("message", "인증이 실패하였습니다. 유효하지 않은 토큰입니다.");
            return response;
        }

        // 사용자 정보 반환
        response.put("success", true);
        response.put("message", "사용자 정보를 조회했습니다.");
        Map<String, String> data = new HashMap<>();
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        response.put("data", data);

        return response;
    }

    // 사용자 정보 수정
    @PatchMapping
    public Map<String, Object> updateUserInfo(@RequestBody UpdateUserRequest request) {
        Map<String, Object> response = new HashMap<>();

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // 이메일은 인증된 사용자 이름으로 사용

        // 사용자 조회
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            response.put("success", false);
            response.put("message", "인증이 실패하였습니다. 유효하지 않은 토큰입니다.");
            return response;
        }

        // 비밀번호 확인
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            response.put("success", false);
            response.put("message", "비밀번호가 일치하지 않습니다.");
            return response;
        }

        // 사용자 정보 업데이트
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));  // 비밀번호 암호화
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "회원 정보가 성공적으로 수정되었습니다.");
        return response;
    }
}
