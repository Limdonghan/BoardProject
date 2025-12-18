package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.PostEntity;
import com.example.BoardProject_back.entity.PostReactionEntity;
import com.example.BoardProject_back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReactionRepository extends JpaRepository<PostReactionEntity,Integer> {

    /// 이 유저가 이 글에 반응을 남긴 적이 있는지 체크
    boolean existsByUserAndPost(UserEntity user, PostEntity post);
}
