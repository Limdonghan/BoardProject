package com.example.BoardProject_back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "post_reaction")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post; // 어떤 글인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user; // 누가 눌렀는지

    @Column(nullable = false)
    private String reactionType; // "LIKE" 또는 "DISLIKE"
}
