package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.PostDTO;
import com.example.BoardProject_back.dto.PostInfoDTO;
import com.example.BoardProject_back.dto.PostUpdateDTO;
import com.example.BoardProject_back.entity.PostEntity;
import com.example.BoardProject_back.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/creation")
    public ResponseEntity postCreation(@Validated @RequestBody PostDTO postDTO, @RequestHeader("Authorization") String token) {
        postService.postCreation(postDTO, token);
        return ResponseEntity.ok("글 작성 완료");
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<PostInfoDTO> postInfo(@PathVariable int id, @RequestHeader(value = "Authorization", required = false ) String token) {
        return ResponseEntity.ok(postService.getPostInfo(id, token));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity postUpdate(
            @PathVariable int id,
            @Validated @RequestBody PostUpdateDTO postUpdateDTO,
            @RequestHeader("Authorization") String token) {
        postService.postUpdate(id,postUpdateDTO,token);
        return ResponseEntity.ok("게시글 수정 완료");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity postDelete(
            @PathVariable int id,
            @RequestHeader("Authorization") String token) {
        postService.postDelete(id,token);
        return ResponseEntity.ok("게시글 삭제 완료");
    }

}
