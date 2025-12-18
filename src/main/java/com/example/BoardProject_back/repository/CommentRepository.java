package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findAllByPostIdOrderByCreatedAtDesc(int postId);

    Optional<CommentEntity> findByIdAndIsDeletedFalse(int id);

    int countByPostIdAndIsDeletedFalse(int postId);

}
