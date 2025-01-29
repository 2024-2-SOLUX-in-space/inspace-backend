package jpabasic.inspacebe.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.keyPath}")
    private String serviceAccountKey;

    @Value("${firebase.storage-bucket}")
    private String storageBucket;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        // Firebase가 이미 초기화되었는지 확인
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount = getClass().getResourceAsStream(serviceAccountKey);

            if (Objects.isNull(serviceAccount)) {
                throw new NullPointerException("Service account is null");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(storageBucket) // Cloud Storage 버킷 URL
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }
}
