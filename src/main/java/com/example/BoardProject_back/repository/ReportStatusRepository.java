package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.ReportStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportStatusRepository extends JpaRepository<ReportStatusEntity,Integer> {

    Optional<ReportStatusEntity> findByCode(String code);
}
