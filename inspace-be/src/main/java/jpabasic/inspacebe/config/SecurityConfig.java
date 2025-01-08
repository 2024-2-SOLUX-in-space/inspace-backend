package jpabasic.inspacebe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/v3/api-docs/**", // Swagger 문서
                        "/swagger-ui/**",  // Swagger UI
                        "/swagger-ui.html", // Swagger HTML
                        "/api/auth/signup", // 회원가입 API
                        "/api/auth/login" // 로그인 API
                ).permitAll() // 위 경로는 인증 없이 접근 가능
                .anyRequest().authenticated() // 그 외 요청은 인증 필요
                .and()
                .formLogin().disable() // 기본 로그인 폼 비활성화
                .httpBasic().disable(); // HTTP Basic 인증 비활성화

        return http.build();
    }
}
