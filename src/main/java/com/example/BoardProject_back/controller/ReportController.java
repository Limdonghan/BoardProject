package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.ReportDTO;
import com.example.BoardProject_back.dto.ReportStatusDTO;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity createPostReport(
            @PathVariable int postId,
            @Validated @RequestBody ReportDTO reportDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        reportService.createPostReport(postId, reportDTO, customUserDetails.getUserEntity());
        return ResponseEntity.ok("게시글 신고 완료");
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity createCommentReport(
            @PathVariable int commentId,
            @Validated @RequestBody ReportDTO reportDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        reportService.createCommentReport(commentId, reportDTO, customUserDetails.getUserEntity());
        return ResponseEntity.ok("댓글 신고 완료");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{reportId}")
    public ResponseEntity statusUpdate(
            @PathVariable int reportId,
            @Validated @RequestBody ReportStatusDTO reportStatusDTO) {
        reportService.changeReportStatus(reportId, reportStatusDTO);
        return ResponseEntity.ok("신고 상태 변경 완료");
    }

}
