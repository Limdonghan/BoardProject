package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.CommentDTO;
import com.example.BoardProject_back.dto.CommentRespDTO;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/{postId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity addComment(
            @PathVariable int postId,
            @RequestBody CommentDTO commentDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ) {
        commentService.CreateComment(postId, commentDTO,customUserDetails.getUserEntity());
        return ResponseEntity.ok("개시글 작성 완료!");

    }

    @GetMapping("/comment")
    public ResponseEntity<List<CommentRespDTO>> getComments(
            @PathVariable int postId,
            Authentication authentication) {

        UserEntity userEntity = null;
        /**
         * 로그인 / 비로그인 확인 로직
         *  1. authentication 객체가 null이 아니고, (보통 null이 아님)
         *  2. 인증된 사용자이며, (로그인 했으며)
         *  3. getPrincipal() 결과가 CustomUserDetails 타입인지 확인
         * */

        if (authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            userEntity = customUserDetails.getUserEntity();
        }
        List<CommentRespDTO> list = commentService.getComments(postId, userEntity);
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/comment/{commendId}")
    public ResponseEntity updateComment(
            @PathVariable int commendId,
            @RequestBody CommentDTO commentDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        commentService.updateComment(commendId, commentDTO,customUserDetails.getUserEntity());
        return ResponseEntity.ok("댓글 수정 완료");
    }

    @DeleteMapping("/comment/{commendId}")
    public ResponseEntity deleteComment(
            @PathVariable int commendId,
            @PathVariable int postId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        commentService.deleteComment(commendId,postId,customUserDetails.getUserEntity());
        return ResponseEntity.ok("댓글 삭제 완료");
    }
}
