package jpabasic.inspacebe.controller;

import jpabasic.inspacebe.dto.RegisterRequest;
import jpabasic.inspacebe.dto.LoginRequest;
import jpabasic.inspacebe.dto.PasswordResetRequest; // 비밀번호 찾기 요청 DTO
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.UserRepository;
import jpabasic.inspacebe.config.JwtProvider;
import jpabasic.inspacebe.service.EmailService; // 이메일 서비스
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private EmailService emailService; // 이메일 서비스

    // 회원가입
    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        // 이메일 중복 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            response.put("success", false);
            response.put("message", "이미 등록된 이메일입니다.");
            return response;
        }

        // 비밀번호 확인
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            response.put("success", false);
            response.put("message", "비밀번호가 일치하지 않습니다.");
            return response;
        }

        // 사용자 생성
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "회원가입이 완료되었습니다! 로그인을 진행해 주세요.");
        return response;
    }

    // 로그인
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        String email = request.getEmail();
        String password = request.getPassword();

        // 사용자 조회
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            response.put("success", false);
            response.put("message", "이메일 또는 비밀번호가 잘못되었습니다.");
            return response;
        }

        // JWT 생성
        String accessToken = jwtProvider.generateAccessToken(email);
        String refreshToken = jwtProvider.generateRefreshToken(email);

        response.put("success", true);
        response.put("message", "로그인 성공");
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("access_token", accessToken);
        data.put("refresh_token", refreshToken);
        response.put("data", data);

        return response;
    }

    // 비밀번호 찾기 (임시 비밀번호 생성 및 이메일 전송)
    @PostMapping("/forgot-password")
    public Map<String, Object> forgotPassword(@RequestBody PasswordResetRequest request) {
        Map<String, Object> response = new HashMap<>();

        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            response.put("success", false);
            response.put("message", "이메일이 존재하지 않습니다.");
            return response;
        }

        // 임시 비밀번호 생성
        String temporaryPassword = generateTemporaryPassword();

        // 사용자 비밀번호를 임시 비밀번호로 업데이트 (암호화하여 저장)
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);

        // 임시 비밀번호 이메일로 전송
        emailService.sendPasswordResetEmail(
                user.getEmail(),
                "임시 비밀번호 : " + temporaryPassword
        );

        response.put("success", true);
        response.put("message", "임시 비밀번호가 이메일로 전송되었습니다. 로그인 후 비밀번호를 변경해 주세요.");
        return response;
    }

    // 임시 비밀번호 생성 메서드
    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // 8자리 랜덤 문자열 생성
    }

    // 로그아웃
    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestHeader("Authorization") String authorizationHeader) {
        Map<String, Object> response = new HashMap<>();

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.put("success", false);
            response.put("message", "토큰이 없습니다.");
            return response;
        }

        // 토큰에서 "Bearer " 부분을 제거하고 실제 토큰 값만 추출
        String token = authorizationHeader.substring(7);

        // 로그아웃 처리를 위해 토큰을 무효화하는 로직을 추가할 수 있습니다
        // 예를 들어, 블랙리스트에 토큰을 추가하거나, 별도의 세션에서 토큰을 제거하는 방법 등을 사용할 수 있습니다.

        // 여기서는 클라이언트가 토큰을 삭제하는 방식만 처리
        response.put("success", true);
        response.put("message", "로그아웃이 성공적으로 완료되었습니다.");
        return response;
    }
}
