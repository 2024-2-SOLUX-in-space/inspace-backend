package jpabasic.inspacebe.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/// https://console.firebase.google.com/project/inspace-d2239/storage/inspace-d2239.firebasestorage.app/files?hl=ko

/// 예외처리->FirebaseConfig 오류로 인해 애플리케이션 실행이 중단되지 않도록 함.
@Configuration
public class FirebaseConfig {
    @Bean
    public void initializeFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            // 예외 처리
            System.err.println("Firebase initialization failed: " + e.getMessage());
        }
    }
}
