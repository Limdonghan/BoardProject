package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.entity.GradeEntity;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.repository.GradeRepository;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    /**
     * 포인트 기준에 따라 등급 자동 승급
     * Bronze	0~99
     * Silver	100~299
     * Gold	    300~699
     * Platinum	700 ~ 999
     * diamond	1000 이상
     */
    @Override
    @Transactional
    public void gradeAssessment(UserEntity userEntity) {
        UserEntity user = userRepository.findById(userEntity.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원 없음!@!@"));

        GradeEntity newGrade = gradeRepository.findGradeByPoint(userEntity.getPoint())
                .orElseThrow(() -> new IllegalArgumentException("너 포인트 이상해"));

        /// 등급 심사
        if (!newGrade.equals(user.getGrade())) {
            user.setGrade(newGrade);
        }

    }
}