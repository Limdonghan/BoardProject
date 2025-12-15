package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.*;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.PostService;
import com.example.BoardProject_back.service.TypesenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final TypesenseService typesenseService;

    @PostMapping()
    public ResponseEntity postCreation(
            @Validated @RequestBody PostDTO postDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ) {
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
        postService.postUpdate(id, postUpdateDTO, customUserDetails.getUserEntity());
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
    ) {
        postService.handleReaction(id, customUserDetails.getUserEntity(), postReactionDTO);
        return ResponseEntity.ok("반영완료");
    }

    @GetMapping("my")
    public MyPostListDTO getMyPostList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return postService.getMyPostList(customUserDetails.getUserEntity());
    }

    @GetMapping("/lists")
    public ResponseEntity<Page<PostListPageDTO>> getBoardList(
            /// page: 현재 페이지 (0부터 시작), size: 한 페이지 게시글 수, sort: 정렬 기준, direction: 정렬 방식
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostListPageDTO> list = postService.getBoardList(pageable);
        return ResponseEntity.ok(list);

    }

    @GetMapping("/{categoryId}/lists")
    public ResponseEntity<Page<PostListPageDTO>> getCategoryBoardList(
            @PathVariable int categoryId,
            /// page: 현재 페이지 (0부터 시작), size: 한 페이지 게시글 수, sort: 정렬 기준, direction: 정렬 방식
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostListPageDTO> list = postService.getCategoryBoardList(pageable, categoryId);
        return ResponseEntity.ok(list);

    }

    @GetMapping("/search")
    public ResponseEntity<List<PostSearchResultDTO>> searchPosts(@RequestParam("query") String query) {
        return ResponseEntity.ok(typesenseService.search(query));
    }
}
