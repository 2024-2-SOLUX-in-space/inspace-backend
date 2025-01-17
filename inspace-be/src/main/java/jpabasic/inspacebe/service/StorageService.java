package jpabasic.inspacebe.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StorageService {

    /// firebase에 이미지를 업로드하고 파일의 url 생성
    public String uploadImage(MultipartFile file,String fileName) {
        try{
            /// Firebase Cloud Storage 버킷 가져오기
            Bucket bucket= StorageClient.getInstance().bucket();

            /// 파일 업로드
            Blob blob=bucket.create(fileName,file.getInputStream(),file.getContentType());

            /// 업로드된 파일의 접근 가능한 URL 생성
            return String.format("https://storage.googleapis.com/%s/%s",bucket.getName(),fileName);
        }catch(IOException e){
            throw new RuntimeException("이미지 업로드에 실패했어요.",e);
        }
    }


}
