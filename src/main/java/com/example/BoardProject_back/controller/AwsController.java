package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.ImageDeleteRequest;
import com.example.BoardProject_back.service.AwsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class AwsController {

    private final AwsService awsService;

    @PostMapping("/upload")
    @Operation(summary = "파일 업로드", description = "s3 파일 업로드 API")
    public ResponseEntity<List<String>> uploadImage(@RequestParam("file") List<MultipartFile> file) {
        List<String> uploadedFileNames = awsService.uploadFile(file);
        return ResponseEntity.ok(uploadedFileNames);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "파일 삭제", description = "s3 파일 삭제 API")
    public ResponseEntity<String> deleteImage(@RequestBody ImageDeleteRequest imageUrls) {
        awsService.deleteFile(imageUrls.getImageUrls());
        return ResponseEntity.ok("이미지 삭제 성공");
    }
}
