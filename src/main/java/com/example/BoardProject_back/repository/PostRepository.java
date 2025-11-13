package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity,Integer> {
    Optional<PostEntity> findByIdAndIsDeletedFalse(int id);
}
