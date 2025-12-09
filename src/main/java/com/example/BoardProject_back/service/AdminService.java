package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.ReportDetailDTO;
import com.example.BoardProject_back.dto.ReportListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    Page<ReportListDTO>  getReportList(Pageable pageable);

    ReportDetailDTO getReportDetail(int reportId);
}
