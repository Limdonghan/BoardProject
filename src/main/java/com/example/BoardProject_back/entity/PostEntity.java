package com.example.BoardProject_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Posts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String title;  // 게시글 제목

    @ManyToOne(fetch = FetchType.LAZY) // category_id 컬럼 (N:1 관계)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;  // 게시글 카테고리

    @ManyToOne(fetch = FetchType.LAZY) // user_id 컬럼 (N:1 관계)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;  // 게시글 작성자

    @Lob  // TEXT타입
    @Column(nullable = false)
    private String context;  // 게시글 본문 내용

    @Column(name = "post_view", nullable = false)
    @ColumnDefault("0")
    private int postView;  // 게시글 조회수

    @Column(name = "like_count", nullable = false)
    @ColumnDefault("0")
    private int likeCount;  // 게시글 좋아요 수

    @Column(name = "dislike_count", nullable = false)
    @ColumnDefault("0")
    private int disLikeCount;  // 게시글 싫어요 수

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;  // 게시글 삭제여부 TRUE = 삭제

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  //  게시글 생성날짜

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  //  게시글 수정날짜

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;  //  게시글 삭제날짜

    /// 게시글 수정
    public void postUpdate(String title, String context, CategoryEntity category){
        this.title = title;
        this.context = context;
        this.category = category;
    }

    ///  게시글 삭제
    public void postDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    /// 좋아요 / 싫어요 증가
    public void likeHandle(int likeCount){
        this.likeCount += 1;
    }

    public void disLikeHandle(int disLikeCount){
        this.disLikeCount += 1;
    }

}
