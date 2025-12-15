package com.example.BoardProject_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchResultDTO {  /// typesense response DTO
    private int postId;
    private String title;               /// 게시글 제목
    private String content;             /// 게시글 본문
    private String author;              /// 게시글 작성자
    private String category;            /// 게시글 카테고리
    private int viewCount;              /// 조회수
    private int totalComments;          /// 총 댓글 수
    private int totalLikes;             /// 총 종아요 수
    private LocalDateTime createdAt;    /// 생성 일자

    private List<String> imageURL;      /// [추가] 이미지
}
