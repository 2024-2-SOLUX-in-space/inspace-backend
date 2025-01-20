package jpabasic.inspacebe.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/// https://console.firebase.google.com/project/inspace-d2239/storage/inspace-d2239.firebasestorage.app/files?hl=ko
@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account-key}")
    private String serviceAccountKey;

    @Value("${firebase.storage-bucket}")
    private String storageBucket;


    @PostConstruct
    public void initializeFirebase() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(serviceAccountKey);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket) // Cloud Storage 버킷 URL
                .build();

        FirebaseApp.initializeApp(options);
    }
}
