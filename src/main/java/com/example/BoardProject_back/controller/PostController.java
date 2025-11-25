package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.*;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping()
    public ResponseEntity postCreation(@Validated @RequestBody PostDTO postDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postService.postCreation(postDTO, customUserDetails.getUserEntity());
        return ResponseEntity.ok("글 작성 완료");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostInfoDTO> postInfo(@PathVariable int id) {
        return ResponseEntity.ok(postService.getPostInfo(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity postUpdate(
            @PathVariable int id,
            @Validated @RequestBody PostUpdateDTO postUpdateDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postService.postUpdate(id, postUpdateDTO,customUserDetails.getUserEntity());
        return ResponseEntity.ok("게시글 수정 완료");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity postDelete(
            @PathVariable int id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postService.postDelete(id, customUserDetails.getUserEntity());
        return ResponseEntity.ok("게시글 삭제 완료");
    }

    @PostMapping("/{id}/reactions")
    public ResponseEntity postReaction(
            @PathVariable int id,
            @RequestBody PostReactionDTO postReactionDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        postService.handleReaction(id,customUserDetails.getUserEntity(),postReactionDTO);
        return ResponseEntity.ok("반영완료");
    }

    @GetMapping("my")
    public MyPostListDTO getMyPostList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return postService.getMyPostList(customUserDetails.getUserEntity());
    }

}
