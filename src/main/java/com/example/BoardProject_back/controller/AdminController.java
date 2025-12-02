package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.entity.PostEntity;
import com.example.BoardProject_back.repository.PostRepository;
import com.example.BoardProject_back.service.TypesenseService;
import lombok.RequiredArgsConstructor;
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

    /// 이 URL을 브라우저나 Postman에서 한번만 호출하면 됩니다.
    /// 예: http://localhost:8080/admin/sync-typesense
    @GetMapping("/sync-typesense")
    public String syncAllData() {
        /// 1. DB에 있는 모든 글을 가져오기 (JPA)
        List<PostEntity> allPosts = postRepository.findAll();

        /// 2. Typesense 서비스에 넘겨서 루프 돌리며 저장시킨다
        typesenseService.indexAllPosts(allPosts);

        return "동기화 완료! 총 " + allPosts.size() + "개의 글이 인덱싱되었습니다.";
    }
}
