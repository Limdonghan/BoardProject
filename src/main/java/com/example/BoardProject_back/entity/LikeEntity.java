package com.example.BoardProject_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Likes",  uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_user_post",
                columnNames = {"user_id", "post_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)  // user_id 컬럼 (N:1 관계)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;  // 좋아요를 클릭한 유저

    @ManyToOne(fetch = FetchType.LAZY)  // post_id 컬럼 (N:1 관계)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;  // 좋아요를 눌린 게시글

    private Boolean type;  // TRUE = 좋아요 , FALSE = 싫어요

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  //  좋아요 생성 날짜

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  //  좋아요 수정 날짜

}
