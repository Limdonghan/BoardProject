package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity,Integer> {

    Page<ReportEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
