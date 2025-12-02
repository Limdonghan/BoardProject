package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.ReportListDTO;
import com.example.BoardProject_back.entity.PostEntity;
import com.example.BoardProject_back.repository.PostRepository;
import com.example.BoardProject_back.service.AdminService;
import com.example.BoardProject_back.service.TypesenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final PostRepository postRepository;
    private final TypesenseService typesenseService;
    private final AdminService adminService;

    @GetMapping("/sync-typesense")
    public String syncAllData() {
        /// 1. DB에 있는 모든 글을 가져오기 (JPA)
        List<PostEntity> allPosts = postRepository.findAll();

        /// 2. Typesense 서비스에 넘겨서 루프 돌리며 저장시킨다
        typesenseService.indexAllPosts(allPosts);

        return "동기화 완료! 총 " + allPosts.size() + "개의 글이 인덱싱되었습니다.";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<Page<ReportListDTO>> getReportList(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReportListDTO> reportList = adminService.getReportList(pageable);

        return ResponseEntity.ok(reportList);
    }
}
