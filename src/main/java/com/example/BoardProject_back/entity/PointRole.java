package com.example.BoardProject_back.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointRole {
    POST_CREATION(10),         /// 게시글 작성
    WRITING_COMMENTS(3),       /// 댓글 작성
    LIKE(5),                   /// 좋아요 받음
    DISLIKE(-5),               /// 싫어요 받음
    REPORT_ACCUMULATION(-5),   /// 신고 누적
    ADMINISTRATOR_WARNING(-10);/// 관리자 경고

    private final int point;
}
