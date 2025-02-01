package jpabasic.inspacebe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",   // 기존 프론트엔드
                        "http://3.35.10.158:8000"  // Nginx 프록시
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 모든 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true); // 인증 요청 허용
    }
}