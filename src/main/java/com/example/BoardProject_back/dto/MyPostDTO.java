package com.example.BoardProject_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MyPostDTO {
    private final int id;                     /// 글 ID (상세보기 링크용)
    private final String authorName;          /// 작성자 (UserEntity.nickname)
    private final String title;               /// 글 제목
    private final String category;            /// 글 카테고리 (Enum이나 String)
    private final int viewCount;              /// 조회수
    private final int likeCount;              /// 좋아요 수
    private final LocalDateTime createDate;   /// 생성 날짜

    private final int commentCount;           /// 이 글에 달린 댓글 수

    private final String thumbnailUrl;        /// 이미지 썸네일

    }