package com.example.BoardProject_back.dto;

import com.example.BoardProject_back.entity.ReportEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReportListDTO {
    private final int id;                     /// 신고 ID
    private final String title;               /// 신고된 게시글 제목
    private final String category;            /// 신고된 게시글 카테고리
    private final String comment;             /// 신고된 댓글
    private final String reported;            /// 신고된 게시글 작성자
    private final String reporter;            /// 신고한 유저
    private final String reportStatus;        /// 신고 상태
    private final LocalDateTime createdDate;  /// 신고 일시

    public ReportListDTO(ReportEntity reportEntity) {

        if (reportEntity.getPost() == null) {
            this.title = null;
            this.category = null;
        }else {
            this.title = reportEntity.getPost().getTitle();
            this.category = reportEntity.getPost().getCategory().getCategory();
        }
        if (reportEntity.getComment() == null) {
            this.comment = null;
        }else {
            this.comment = reportEntity.getComment().getComment();
        }

        this.id = reportEntity.getId();
        this.reported = reportEntity.getReported().getNickName();
        this.reporter = reportEntity.getReporter().getNickName();
        this.reportStatus = reportEntity.getStatus().getCodeName();
        this.createdDate = reportEntity.getCreatedAt();
    }

}
