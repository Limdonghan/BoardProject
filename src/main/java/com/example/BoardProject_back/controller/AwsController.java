package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.service.AwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class AwsController {

    private final AwsService awsService;

    @PostMapping("upload")
    public ResponseEntity<List<String>> uploadImage(@RequestParam("file") List<MultipartFile> file) {
        List<String> uploadedFileNames = awsService.uploadFile(file);
        return ResponseEntity.ok(uploadedFileNames);
    }
}
