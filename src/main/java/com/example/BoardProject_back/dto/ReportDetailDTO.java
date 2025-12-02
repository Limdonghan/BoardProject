package com.example.BoardProject_back.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDetailDTO {
    private final int reportId;                    /// 신고 ID
    private final PostInfoDTO postInfo;            /// 게시글 정보
    private final String comment;                  /// 신고된 댓글 내용
    private final String reported;                 /// 신고당한 유저
    private final ReportStatusSummaryDTO summary;  /// 신고 요약 정보


}
