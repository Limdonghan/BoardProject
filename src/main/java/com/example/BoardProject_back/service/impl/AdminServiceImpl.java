package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.*;
import com.example.BoardProject_back.entity.ImageEntity;
import com.example.BoardProject_back.entity.PostEntity;
import com.example.BoardProject_back.entity.ReportEntity;
import com.example.BoardProject_back.repository.ImageRepository;
import com.example.BoardProject_back.dto.ReportDetailDTO;
import com.example.BoardProject_back.dto.ReportListDTO;
import com.example.BoardProject_back.dto.ReportStatusSummaryDTO;
import com.example.BoardProject_back.entity.CommentEntity;
import com.example.BoardProject_back.repository.CommentRepository;
import com.example.BoardProject_back.repository.PostRepository;
import com.example.BoardProject_back.repository.ReportRepository;
import com.example.BoardProject_back.service.AdminService;
import com.example.BoardProject_back.service.TypesenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ReportRepository reportRepository;
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TypesenseService typesenseService;

    /**
     * 신고 페이지네이션 - 전체
     */
    @Override
    public Page<ReportListDTO> getReportList(Pageable pageable) {
        Page<ReportEntity> reportPage = reportRepository.findAllByOrderByCreatedAtDesc(pageable);
        return reportPage.map(report -> new ReportListDTO(report));
    }

    /**
     * 신고 페이지네이션 - 신고상태별
     */
    @Override
    public Page<ReportListDTO> getReportStatusList(Pageable pageable, int statusId) {
        Page<ReportEntity> reportPage = reportRepository.findAllByStatus_IdOrderByCreatedAtDesc(pageable, statusId);
        return reportPage.map(report -> new ReportListDTO(report));
    }

    /**
     * 신고 페이지네이션 - 게시글 및 신고상태별 댓글 및 신고상태별
     */
    @Override
    public Page<ReportListDTO> getPostAndStatusList(Pageable pageable, int statusId) {
        Page<ReportEntity> reportPage = reportRepository.findAllByStatus_IdAndPostIsNotNullOrderByCreatedAtDesc(pageable, statusId);
        return reportPage.map(report -> new ReportListDTO(report));
    }

    /**
     * 신고 페이지네이션 - 댓글 및 신고상태별
     */
    @Override
    public Page<ReportListDTO> getCommentAndStatusList(Pageable pageable, int statusId) {
        Page<ReportEntity> reportPage = reportRepository.findAllByStatus_IdAndCommentIsNotNullOrderByCreatedAtDesc(pageable, statusId);
        return reportPage.map(report -> new ReportListDTO(report));
    }
    /**
     * 신고 페이지네이션 - 댓글 및 신고상태별
     */


    /**
     * 신고 상세 페이지
     */
    @Override
    public ReportDetailDTO getReportDetail(int reportId) {

        /// 신고 DB 찾기
        ReportEntity reportEntity = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        boolean isPostReport = false;
        /// 이 신고가 어떤(게시글 or 댓글) 신고인지 판단
        if (reportEntity.getPost() != null) {
            isPostReport = true;
        }

        /// isPostReport가 true면 게시글 내용 저장, false면 댓글의 게시글 내용 저장
        PostEntity postEntity = isPostReport ? reportEntity.getPost() : reportEntity.getComment().getPost();

        /// 게시글, 댓글 별 총 신고된 수 계산
        int totalReportCount = isPostReport
                ? reportRepository.countByPostId(postEntity.getId())
                : reportRepository.countByCommentId(reportEntity.getComment().getId());

        ///  전체 신고 목록 조회 + 신고자 리스트 만들기
        List<ReportEntity> reportList = isPostReport
                ? reportRepository.findAllByPostId(postEntity.getId())
                : reportRepository.findAllByCommentId(reportEntity.getComment().getId());

        /// 신고자 닉네임 목록 (중복 제거)
        List<String> reporters = reportList.stream()
                .map(reportEntity1 -> reportEntity1.getReporter().getNickName())
                .distinct()
                .toList();

        /// 신고 사유 목록 (중복 제거)
        List<String> reasons = reportList.stream()
                .map(reportEntity1 -> reportEntity1.getReason().getCodeName())
                .distinct()
                .toList();

        List<ImageEntity> allByPostId = imageRepository.findAllByPostId(postEntity.getId());
        List<String> imgUrlList = allByPostId.stream()
                .map(ImageEntity::getUrl)
                .toList();

        /// DTO 매핑
        return ReportDetailDTO.builder()
                .reportId(reportEntity.getId())
                .comment(reportEntity.getComment() != null ? reportEntity.getComment().getComment() : "")
                .reported(reportEntity.getReported().getNickName())
                .postInfo(reportEntity.getPost() != null
                        ? AdminPostInfoDTO.builder()
                        .user(reportEntity.getReported().getNickName())
                        .title(reportEntity.getPost().getTitle())
                        .category(reportEntity.getPost().getCategory().getCategory())
                        .context(reportEntity.getPost().getContext())
                        .likeCount(reportEntity.getPost().getLikeCount())
                        .disLikeCount(reportEntity.getPost().getDisLikeCount())
                        .date(reportEntity.getPost().getCreatedAt())
                        .isDeleted(reportEntity.getPost().isDeleted())
                        .imageUrl(imgUrlList)
                        .build()
                        : AdminPostInfoDTO.builder()
                        .user(reportEntity.getComment().getUser().getNickName())
                        .title(reportEntity.getComment().getPost().getTitle())
                        .category(reportEntity.getComment().getPost().getCategory().getCategory())
                        .context(reportEntity.getComment().getPost().getContext())
                        .likeCount(reportEntity.getComment().getPost().getLikeCount())
                        .disLikeCount(reportEntity.getComment().getPost().getDisLikeCount())
                        .date(reportEntity.getComment().getPost().getCreatedAt())
                        .isDeleted(reportEntity.getComment().isDeleted())
                        .imageUrl(imgUrlList)
                        .build())
                .summary(ReportStatusSummaryDTO.builder()
                        .reporters(reporters)
                        .status(reportEntity.getStatus().getCodeName())
                        .reasons(reasons)
                        .totalReporterCount(totalReportCount)
                        .build())
                .build();
    }

    /**
     * 게시글 삭제 및 댓글 삭제
     */
    @Override
    @Transactional
    public void adminConsole(int id) {

        ReportEntity reportEntity = reportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Report not found"));

        if (reportEntity.getPost() != null) {
            List<ReportEntity> byPostId = reportRepository.findAllByPostId(reportEntity.getPost().getId());

            /// 게시글 조회
            PostEntity post = postRepository.findById(byPostId.get(0).getId())
                    .orElseThrow(() -> new IllegalArgumentException("이미 삭제된 게시글!"));

            /// 게시글 삭제
            post.postDelete();

            /// typesense 삭제
            typesenseService.deletePost(id);
        } else {
            List<ReportEntity> allByCommentId = reportRepository.findAllByCommentId(reportEntity.getComment().getId());

            /// 댓글 조회
            CommentEntity comment = commentRepository.findById(allByCommentId.get(0).getId())
                    .orElseThrow(() -> new IllegalArgumentException("이미 삭제된 댓글"));

            /// 댓글 삭제
            comment.commentDelete();
        }


    }


}
