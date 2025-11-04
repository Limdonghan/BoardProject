package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity,Integer> {
}
