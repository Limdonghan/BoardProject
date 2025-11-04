package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {




}
