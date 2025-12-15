package com.example.BoardProject_back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    /// S3에 저장된 이미지 객체의 public url을 반환
    public List<String> uploadFile(List<MultipartFile> files) {

        /// 각 파일을 업로드하고 url을 리스트로 반환
        return files.stream()
                .map(multipartFile -> uploadImage(multipartFile))
                .toList();
    }

    /**
     * validateFile메서드를 호출하여 유효성 검증 후
     * uploadImageToS3메서드에 데이터를 반환하여 S3에 파일 업로드
     * public url을 받아 서비스 로직에 반환
     */
    private String uploadImage(MultipartFile file) {
        validateFile(file.getOriginalFilename());
        return uploadImageToS3(file);
    }

    /**
     * 파일 유효성 검증
     */
    private void validateFile (String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("파일 이름이 null입니다");
        }

        if (fileName.contains("..")) {
            throw new RuntimeException("파일 이름에 잘못된 경로 세그먼트가 포함되어 있습니다");
        }

        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            throw new RuntimeException("파일 이름에 확장자가 없습니다");
        }

        /// 허용되지 않는 확장자 검증
        String extension = fileName.substring(lastIndexOf + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
        if (!allowedExtentionList.contains(extension)) {
            throw new RuntimeException("파일 이름에 허용되지 않는 확장자가 포함되어 있습니다. 추출값 : "+extension);
        }
    }

    /**
    * 직접적으로 S3에 업로드
    */
    private String uploadImageToS3(MultipartFile file) {
        /// 원본 파일 명
        String originalFilename = file.getOriginalFilename();

        /// 변경된 파일
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + "_" + originalFilename;

        /// 이미지 파일 -> InputStream 변환
        try(InputStream inputStream = file.getInputStream()) {
            /// putObjectRequest 객체 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)                 /// 버킷이름
                    .key(s3FileName)                    /// 저장할 파일 이름
                    .contentType(file.getContentType()) /// 이미지 MIME 타입
                    .contentLength(file.getSize())      /// 파일 크기
                    .build();
            /// s3에 이미지 업로드
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream,file.getSize()));
        }catch (Exception e){
            log.error("s3 업로드실패: {}",e.getMessage());
            throw new RuntimeException("이미지 업로드에 실패했습니다.");
        }

        /// public url 반환
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(s3FileName)).toString();
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(List<String> imageUrls) {

        /// 리스트가 비어있으면 S3 요청을 보내지 않음
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        /// URL에서 Key 추출
        List<String> keys = imageUrls.stream()
                .map(s -> getKeyFromImageUrls(s))
                .toList();
        try {
            /// 삭제 요청 객체 생성
            DeleteObjectsRequest deleteObjectRequest = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(builder -> builder.objects(
                            keys.stream()
                                    .map(s -> ObjectIdentifier.builder().key(s).build())
                                    .toList()
                    ))
                    .build();

            /// S3에 삭제 요청 전송
            s3Client.deleteObjects(deleteObjectRequest);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("파일 삭제중 오류가 발생했습니다.");
        }
    }

    /**
     * [핵심 로직] 이미지 URL에서 S3 객체 Key(파일명) 추출
     * 예: https://버킷.s3.ap-northeast-2.../test/image.jpg -> test/image.jpg
     */
    private String getKeyFromImageUrls(String imageUrl) {
        try {
            /// 인코딩된 주소를 URI 객체로 변환 후 URL 객체로 변환
            URL url = new URI(imageUrl).toURL();

            /// URI에서 경로 부분을 가져와 URL 디코딩을 통해 실제 키로 변환 (중요! 한글/공백 디코딩)
            String decodedKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);

            /// 경로 앞에 '/'가 있으므로 이를 제거한 뒤 반환 ( /test.jpg -> test.jpg )
            return decodedKey.substring(1);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("허용되지 않은 포멧");
        }
    }
}
