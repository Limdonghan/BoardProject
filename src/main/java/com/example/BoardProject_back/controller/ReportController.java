package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.ReportDTO;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/posts/{postId}")
    @Operation(summary = "게시글 신고", description = "게시글 신고 API")
    public ResponseEntity createPostReport(
            @PathVariable int postId,
            @Validated @RequestBody ReportDTO reportDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        reportService.createPostReport(postId, reportDTO, customUserDetails.getUserEntity());
        return ResponseEntity.ok("게시글 신고 완료");
    }

    @PostMapping("/comments/{commentId}")
    @Operation(summary = "댓글 신고", description = "댓글 신고 API")
    public ResponseEntity createCommentReport(
            @PathVariable int commentId,
            @Validated @RequestBody ReportDTO reportDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        reportService.createCommentReport(commentId, reportDTO, customUserDetails.getUserEntity());
        return ResponseEntity.ok("댓글 신고 완료");
    }

}
