package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity,Integer> {

    Page<ReportEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /// 게시글 총 신고된 횟수
    int countByPostId(int postId);

    /// 댓글 총 신고된 횟수
    int countByCommentId(int commentId);

    /// 해당 게시글 신고자 리스트
    List<ReportEntity> findAllByPostId(int postId);
    
    /// 해당 댓글 신고자 리스트
    List<ReportEntity> findAllByCommentId(int commentId);


}
