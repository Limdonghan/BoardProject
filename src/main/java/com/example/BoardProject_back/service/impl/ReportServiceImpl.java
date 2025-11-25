package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.ReportDTO;
import com.example.BoardProject_back.dto.ReportStatusDTO;
import com.example.BoardProject_back.entity.*;
import com.example.BoardProject_back.repository.*;
import com.example.BoardProject_back.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시글 신고 등록
     */
    @Override
    public void createPostReport(int postId, ReportDTO reportDTO, UserEntity userEntity) {
        /// 게시글 조회
        PostEntity postEntity = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post id not found"));

        saveReport(userEntity, postEntity.getUser(),reportDTO,postEntity,null);
    }

    /**
     * 게시글 신고 등록
     */
    @Override
    public void createCommentReport(int commentId, ReportDTO reportDTO, UserEntity userEntity) {
        /// 댓글 조회
        CommentEntity commentEntity = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment id not found"));

        saveReport(userEntity,commentEntity.getUser(),reportDTO,null,commentEntity);
    }


    /**
     * 공통적인 신고 저장 로직을 추출
     */
    private void saveReport(UserEntity reporter, UserEntity reported, ReportDTO reportDTO, PostEntity postEntity, CommentEntity commentEntity) {

        /// 신고 사유 조회
        ReportReasonEntity reasonId = reportReasonRepository.findById(reportDTO.getReasonId())
                .orElseThrow(() -> new IllegalArgumentException("Reason id not found"));

        /// 신고 상태 조회 (초기값 접수대기)
        ReportStatusEntity statusId = reportStatusRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Status id not found"));

        ReportEntity reportEntity = ReportEntity.builder()
                .reporter(reporter)
                .reported(reported)
                .post(postEntity)
                .comment(commentEntity)
                .reason(reasonId)
                .status(statusId)
                .build();
        reportRepository.save(reportEntity);
    }

    /**
     * 관리자 전용: 신고 처리 및 포인트 차감
     */
    @Override
    @Transactional
    public void changeReportStatus(int reportId, ReportStatusDTO reportStatusDTO) {
        /// 신고 내역 조회
        ReportEntity reportEntity = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report id not found"));

        String targetStatusCode = reportStatusDTO.getStatus();

        ReportStatusEntity newStatus = reportStatusRepository.findByCode(targetStatusCode)
                        .orElseThrow(() -> new IllegalArgumentException("status code not found"));

        /// 신고 상태 변경
        reportEntity.setStatus(newStatus);

        /// Status code가 RESOLVED 경우 포인트 차감
        if (newStatus.getCode().equals("RESOLVED")){
            /// 피신고자 조회
            UserEntity reported = reportEntity.getReported();

            /// 피신고자 포인트 차감
            reported.userAddPoint(PointRole.REPORT_ACCUMULATION);
        }

    }



}
