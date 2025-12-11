package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.ImageDeleteRequest;
import com.example.BoardProject_back.service.AwsService;
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
    public ResponseEntity<List<String>> uploadImage(@RequestParam("file") List<MultipartFile> file) {
        List<String> uploadedFileNames = awsService.uploadFile(file);
        return ResponseEntity.ok(uploadedFileNames);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestBody ImageDeleteRequest imageUrls) {
        awsService.deleteFile(imageUrls.getImageUrls());
        return ResponseEntity.ok("이미지 삭제 성공");
    }
}
