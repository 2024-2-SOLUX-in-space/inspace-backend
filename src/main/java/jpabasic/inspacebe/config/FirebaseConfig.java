package jpabasic.inspacebe.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {


    @Value("${firebase.service-account-key}")
    private String serviceAccountKey;
    @Value("${firebase.storage-bucket}")
    private String storageBucket;
    @PostConstruct
    public void initializeFirebase() throws IOException {
        InputStream serviceAccount =
                new ClassPathResource(serviceAccountKey).getInputStream();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket) // Cloud Storage 버킷 URL
                .build();
        FirebaseApp.initializeApp(options);
    }
}
