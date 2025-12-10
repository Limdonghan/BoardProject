package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity,Integer> {

    /// 페이지네이션 전체
    Page<ReportEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /// 추가 - 페이지네이션 신고상태별
    Page<ReportEntity> findAllByStatus_IdOrderByCreatedAtDesc(Pageable pageable, int statusId);

    /// 게시글 총 신고된 횟수
    int countByPostId(int postId);

    /// 댓글 총 신고된 횟수
    int countByCommentId(int commentId);

    /// 해당 게시글 신고자 리스트
    List<ReportEntity> findAllByPostId(int postId);

    /// 해당 댓글 신고자 리스트
    List<ReportEntity> findAllByCommentId(int commentId);

    /// 유저가 게시글 or 댓글에 신고를 한 적 있는지 체크(한번만 가능하도록)
    boolean existsByReporterIdAndPostId(int reporterId, int postId);
    boolean existsByReporterIdAndCommentId(int reporterId, int commentId);

    /// 페이지네이션 게시글 및 신고상태별
    Page<ReportEntity> findAllByStatus_IdAndPostIsNotNullOrderByCreatedAtDesc(Pageable pageable, int statusId);

    /// 페이지네이션 댓글 및 신고상태별
    Page<ReportEntity> findAllByStatus_IdAndCommentIsNotNullOrderByCreatedAtDesc(Pageable pageable, int statusId);
}
