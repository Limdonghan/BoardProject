package com.example.BoardProject_back.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReportStatusSummaryDTO {
    List<String> reporters;                   /// 신고자
    private final int totalReporterCount;     /// 총 신고된 수
    private final String status;              /// 신고 상태
    List<String> reasons;                     /// 신고 이유

}
