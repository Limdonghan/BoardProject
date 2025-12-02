package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.ReportListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    public Page<ReportListDTO>  getReportList(Pageable pageable);
}
