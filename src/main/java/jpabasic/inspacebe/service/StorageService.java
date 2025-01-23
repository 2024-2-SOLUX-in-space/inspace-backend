package jpabasic.inspacebe.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import jakarta.transaction.Transactional;
import jpabasic.inspacebe.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final ItemRepository itemRepository;


    @Value("${firebase.storage-bucket}")
    private String storageBucket;


    @Transactional
    public Map<String, String> uploadImage(MultipartFile file, String itemId) {
        Map<String, String> result = new HashMap<>();
        String imageUrl;
        String uniqueFileName;

        try {
            // Firebase Cloud Storage 버킷 가져오기
            Bucket bucket = StorageClient.getInstance().bucket();
            String randomUUID = UUID.randomUUID().toString();

            // 고유한 UUID를 파일 이름으로 생성 (UUID를 사용하지 않고 원본 파일명을 그대로 사용할 수도 있음)
            uniqueFileName = randomUUID + "-" + file.getOriginalFilename();

            // 파일 업로드
            Blob blob = bucket.create(uniqueFileName, file.getInputStream(), file.getContentType());

            // Firebase Storage URL 생성 (URL 인코딩 없이 파일명을 그대로 사용)
            imageUrl = String.format(
                    "https://firebasestorage.googleapis.com/v0/b/%s.firebasestorage.app/o/%s?alt=media",
                    "inspace-d2239",  // 버킷 이름을 정확히 지정
                    URLEncoder.encode(uniqueFileName, StandardCharsets.UTF_8)
                            .replace("%20", "+")  // 공백을 '+'로 변경
            );

            // 결과를 Map에 저장
            result.put("randomUUID", randomUUID);
            result.put("imageUrl", imageUrl);

        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했어요.", e);
        }

        return result;
    }


    @Transactional
    public String getImagePath(MultipartFile file, String randomUUID) {

        String fileUrl;
        try {
            // Firebase Cloud Storage 버킷 가져오기
            Bucket bucket = StorageClient.getInstance().bucket();

            // 고유한 UUID를 파일 이름으로 생성 (UUID를 사용하지 않고 원본 파일명을 그대로 사용할 수도 있음)
            String uniqueFileName = randomUUID + "-" + file.getOriginalFilename();

            // Firebase Storage URL 생성 (파일 이름을 그대로 사용)
            fileUrl = String.format("%s", uniqueFileName);

        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드에 실패했어요.", e);
        }

        return fileUrl;
    }


    //파이어베이스 상에서 사진 삭제
    @Transactional
    public void deleteFile(String filePath) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket(storageBucket);


            // 파일 참조 가져오기
            Blob blob = bucket.get(filePath);

            if (blob != null && blob.exists()) {
                boolean deleted = blob.delete();
                if (deleted) {
                    System.out.println("파일이 성공적으로 삭제되었어요");
                } else {
                    System.out.println("파일 삭제에 실패했어요.");
                }
            } else {
                System.out.println("파일을 찾을 수 없어요.");
            }
        } catch (Exception e) {
            System.err.println("파일 삭제중 오류 발생");
        }

    }


}
