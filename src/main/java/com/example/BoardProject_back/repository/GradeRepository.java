package com.example.BoardProject_back.repository;

import com.example.BoardProject_back.entity.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<GradeEntity,Integer> {

    @Query("select g from GradeEntity g where :point between g.minPoints and g.maxPoints")
    Optional<GradeEntity> findGradeByPoint(@Param("point") int point);

}
