package jpabasic.inspacebe.service;

import jpabasic.inspacebe.dto.LoginRequest;
import jpabasic.inspacebe.dto.LoginResponse;
import jpabasic.inspacebe.dto.RegisterRequest;
import jpabasic.inspacebe.model.User;
import jpabasic.inspacebe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 로직
    public void registerUser(RegisterRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .build();

        userRepository.save(user); // 사용자 저장
    }

    // 로그인 로직
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // 로그인 성공 시 액세스 및 리프레시 토큰 반환 (토큰은 임시로 예시 작성)
        return new LoginResponse(
                user.getEmail(),
                "access_token_placeholder",   // 액세스 토큰 (임시)
                "refresh_token_placeholder"  // 리프레시 토큰 (임시)
        );
    }
}
