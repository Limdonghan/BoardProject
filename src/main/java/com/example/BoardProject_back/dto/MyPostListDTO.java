package com.example.BoardProject_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPostListDTO {
    private int id;                     /// 글 ID (상세보기 링크용)
    private String authorName;          /// 작성자 (UserEntity.nickname)
    private String title;               /// 글 제목
    private String category;            /// 글 카테고리 (Enum이나 String)
    private int viewCount;              /// 조회수
    private LocalDateTime createDate;   /// 생성 날짜

    // 여기가 중요합니다 (계산된 값)
    private int totalPostCount;        /// 작성자가 쓴 전체 게시글 수
    private int commentCount;           /// 이 글에 달린 댓글 수
}
