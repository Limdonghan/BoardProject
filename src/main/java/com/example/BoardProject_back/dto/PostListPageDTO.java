package com.example.BoardProject_back.dto;

import com.example.BoardProject_back.entity.PostEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListPageDTO {
    private final int id;
    private final String title;               /// 게시글 제목
    private final String writer;              /// 게시글 작성자
    private final int commentCount;           /// 게시글 총 댓글 수
    private final LocalDateTime createdDate;  /// 게시글 작성일시

    public PostListPageDTO(PostEntity postEntity) {
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.writer = postEntity.getUser().getNickName();
        this.commentCount = postEntity.getCommentCount();
        this.createdDate = postEntity.getCreatedAt();
    }
}
