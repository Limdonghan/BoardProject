package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity,Integer> {
}
