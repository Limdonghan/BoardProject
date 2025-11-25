package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.ReportDTO;
import com.example.BoardProject_back.dto.ReportStatusDTO;
import com.example.BoardProject_back.entity.UserEntity;

public interface ReportService {
    void createPostReport(int postId, ReportDTO reportDTO, UserEntity userEntity);
    void createCommentReport(int commentId, ReportDTO reportDTO, UserEntity userEntity);
    void changeReportStatus(int reportId, ReportStatusDTO reportStatusDTO);



}
