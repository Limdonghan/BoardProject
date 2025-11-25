package com.example.BoardProject_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPostDTO {
    private int id;                     /// 글 ID (상세보기 링크용)
    private String authorName;          /// 작성자 (UserEntity.nickname)
    private String title;               /// 글 제목
    private String category;            /// 글 카테고리 (Enum이나 String)
    private int viewCount;              /// 조회수
    private LocalDateTime createDate;   /// 생성 날짜

    private int commentCount;           /// 이 글에 달린 댓글 수
}
