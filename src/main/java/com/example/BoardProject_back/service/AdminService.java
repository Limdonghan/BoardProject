package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.ReportDetailDTO;
import com.example.BoardProject_back.dto.ReportListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    Page<ReportListDTO> getReportList(Pageable pageable);
    Page<ReportListDTO> getReportStatusList(Pageable pageable, int statusId);
    Page<ReportListDTO> getPostAndStatusList(Pageable pageable, int statusId);
    Page<ReportListDTO> getCommentAndStatusList(Pageable pageable, int statusId);

    ReportDetailDTO getReportDetail(int reportId);

    void adminConsole (int id);
}
