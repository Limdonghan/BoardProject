package com.example.BoardProject_back.dto;

import com.example.BoardProject_back.entity.PostEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListPageDTO {
    private final int id;
    private final String title;               /// 게시글 제목
    private final String category;            /// 게시글 카테고리
    private final int postView;               /// 게시글 조회수
    private final int likeCount;              /// 게시글 좋아요 수
    private final String writer;              /// 게시글 작성자
    private final int commentCount;           /// 게시글 총 댓글 수
    private final LocalDateTime createdDate;  /// 게시글 작성일시

    public PostListPageDTO(PostEntity postEntity) {

        /// 좋아요 수 계산
        int totalLike = postEntity.getLikeCount() - postEntity.getDisLikeCount();

        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.category = postEntity.getCategory().getCategory();
        this.postView = postEntity.getPostView();
        this.likeCount = totalLike;
        this.writer = postEntity.getUser().getNickName();
        this.commentCount = postEntity.getCommentCount();
        this.createdDate = postEntity.getCreatedAt();
    }
}
