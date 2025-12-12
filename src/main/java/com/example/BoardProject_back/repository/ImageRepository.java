package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

    List<ImageEntity> findAllByPostId(int postId);
}
