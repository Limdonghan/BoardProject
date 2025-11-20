package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity,Integer> {
    Optional<PostEntity> findByIdAndIsDeletedFalse(int id);


    @Modifying(clearAutomatically = true) /// 쿼리 실행 후 영속성 컨텍스트 초기화 (중요!)
    @Query("update PostEntity p set p.postView = p.postView + 1, p.updatedAt=p.updatedAt where p.id = :id")
    void increaseViewCount(@Param("id") int id);  /// 조회수 증가

    @Modifying(clearAutomatically = true)
    @Query("update PostEntity p set p.likeCount = p.likeCount + 1, p.updatedAt = p.updatedAt where  p.id = :id")
    void increaseLikeCount(@Param("id") int id);  /// 좋아요 증가

    @Modifying(clearAutomatically = true)
    @Query("update PostEntity p set p.disLikeCount = p.disLikeCount + 1, p.updatedAt = p.updatedAt where  p.id = :id")
    void increaseDisLikeCount(@Param("id") int id);  /// 싫어요 증가
}
