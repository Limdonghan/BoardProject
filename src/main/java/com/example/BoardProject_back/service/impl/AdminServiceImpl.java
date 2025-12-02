package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.ReportListDTO;
import com.example.BoardProject_back.entity.ReportEntity;
import com.example.BoardProject_back.repository.ReportRepository;
import com.example.BoardProject_back.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ReportRepository reportRepository;

    @Override
    public Page<ReportListDTO> getReportList(Pageable pageable) {
        Page<ReportEntity> reportPage = reportRepository.findAllByOrderByCreatedAtDesc(pageable);

        return reportPage.map(report->new ReportListDTO(report));
    }
}
