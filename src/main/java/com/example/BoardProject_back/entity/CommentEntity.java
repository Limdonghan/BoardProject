package com.example.BoardProject_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comments")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY) // post_id 컬럼 (N:1 관계)
    @JoinColumn(name = "post_id",  nullable = false)
    private PostEntity post;  // 댓글이 달린 게시글

    @ManyToOne(fetch = FetchType.LAZY) // user_id 컬럼 (N:1 관계)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;  // 댓글 작성자

    @Lob  // TEXT타입
    @Column(nullable = false)
    private String comment;  // 댓글 내용

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;  // 댓글 삭제여부 TRUE = 삭제

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  //  댓글 생성날짜

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  //  댓글 수정날짜

    private LocalDateTime deletedAt;  //  댓글 삭제날짜

    ///  댓글 수정
    public void commentUpdate(String comment) {
        this.comment = comment;
    }

    /// 댓글 삭제
    public void commentDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
