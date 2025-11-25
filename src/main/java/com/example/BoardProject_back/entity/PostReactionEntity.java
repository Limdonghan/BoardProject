package com.example.BoardProject_back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "post_reaction")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostReactionEntity {

    /// 한 사람이 한 게시글에 중복으로 좋아요 누르는 것을 DB 차원에서도 막기 위해 설정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post; /// 어떤 게시글에 대한 반응인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user; /// 누가 눌렀는지 (로그인한 사용자)

    @Column(nullable = false)
    private String reactionType; /// "LIKE" 또는 "DISLIKE"
}
