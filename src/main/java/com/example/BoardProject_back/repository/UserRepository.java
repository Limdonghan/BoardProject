package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {


    ///  SpringSecurity 유저 검색용 ID( email )
    @Query("select m from UserEntity m where m.email=:email")
    Optional<UserEntity> findByEmail(@Param("email") String email);


}
