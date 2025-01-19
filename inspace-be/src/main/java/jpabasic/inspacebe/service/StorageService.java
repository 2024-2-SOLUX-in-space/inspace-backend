package jpabasic.inspacebe.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.io.IOException;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import java.io.IOException;

@Service
public class StorageService {



    public String uploadImage(MultipartFile file) {
        try {
            // Firebase Cloud Storage 버킷 가져오기
            Bucket bucket = StorageClient.getInstance().bucket();

            // 고유한 UUID를 파일 이름으로 생성
            String uniqueFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            // 파일 업로드
            Blob blob = bucket.create(uniqueFileName, file.getInputStream(), file.getContentType());

            // 업로드된 파일의 접근 가능한 URL 생성
            return String.format("https://firebasestorage.storage.googleapis.com/v0/b/%s/o/%s", bucket.getName(), uniqueFileName);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했어요.", e);
        }
    }



}
