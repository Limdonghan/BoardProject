package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
}
