package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.ReportDetailDTO;
import com.example.BoardProject_back.dto.ReportListDTO;
import com.example.BoardProject_back.dto.ReportStatusDTO;
import com.example.BoardProject_back.entity.PostEntity;
import com.example.BoardProject_back.repository.PostRepository;
import com.example.BoardProject_back.service.AdminService;
import com.example.BoardProject_back.service.ReportService;
import com.example.BoardProject_back.service.TypesenseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final PostRepository postRepository;
    private final TypesenseService typesenseService;
    private final AdminService adminService;
    private final ReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sync-typesense")
    @Operation(summary = "기존 게시글 인덱싱", description = "로컬에서 진행한 DB내용 Typesense에 인덱싱")
    public String syncAllData() {
        /// 1. DB에 있는 모든 글을 가져오기 (JPA)
        List<PostEntity> allPosts = postRepository.findAll();

        /// 2. Typesense 서비스에 넘겨서 루프 돌리며 저장시킨다
        typesenseService.indexAllPosts(allPosts);

        return "동기화 완료! 총 " + allPosts.size() + "개의 글이 인덱싱되었습니다.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    @Operation(summary = "신고목록", description = "신고목록 전체 조회 API")
    public ResponseEntity<Page<ReportListDTO>> getReportList(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReportListDTO> reportList = adminService.getReportList(pageable);

        return ResponseEntity.ok(reportList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{statusId}")
    @Operation(summary = "신고목록 전체", description = "신고목록 신고 상태별 조회 API")
    public ResponseEntity<Page<ReportListDTO>> getReportStatusList(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable int statusId) {
        Page<ReportListDTO> reportList = adminService.getReportStatusList(pageable, statusId);

        return ResponseEntity.ok(reportList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("detail/{reportId}")
    @Operation(summary = "신고관리 상세", description = "신고목록 신고 상태별 조회 API")
    public ResponseEntity<ReportDetailDTO> getReportDetailDTOResponseEntity(@PathVariable int reportId) {
        ReportDetailDTO reportDetail = adminService.getReportDetail(reportId);
        return ResponseEntity.ok(reportDetail);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("post/{statusId}")
    @Operation(summary = "신고목록 게시글 및 신고상태별", description = "신고목록 게시글 및 신고상태별 조회 API")
    public ResponseEntity<Page<ReportListDTO>> getReportListPostAndStatusDTOResponseEntity(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable int statusId) {
        Page<ReportListDTO> reportList = adminService.getPostAndStatusList(pageable, statusId);
        return ResponseEntity.ok(reportList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("comment/{statusId}")
    @Operation(summary = "신고목록 댓글 및 신고상태별", description = "신고목록 댓글 및 신고상태별 조회 API")
    public ResponseEntity<Page<ReportListDTO>> getReportListCommentAndStatusDTOResponseEntity(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable int statusId) {
        Page<ReportListDTO> reportList = adminService.getCommentAndStatusList(pageable, statusId);
        return ResponseEntity.ok(reportList);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 및 댓글 삭제", description = "관리자 전용 게시글 및 댓글 삭제 API")
    public ResponseEntity adminDelete(@PathVariable int id) {
        adminService.adminConsole(id);
        return ResponseEntity.ok("관리자 삭제 완료");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{reportId}")
    @Operation(summary = "신고 처리", description = "게시글 상태 변경 및 신고 처리 API")
    public ResponseEntity statusUpdate(
            @PathVariable int reportId,
            @Validated @RequestBody ReportStatusDTO reportStatusDTO) {
        reportService.changeReportStatus(reportId, reportStatusDTO);
        return ResponseEntity.ok("신고 상태 변경 완료");
    }
}
