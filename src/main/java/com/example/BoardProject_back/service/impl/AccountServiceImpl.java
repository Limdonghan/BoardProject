package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.CreateDTO;
import com.example.BoardProject_back.entity.GradeEntity;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.entity.UserRole;
import com.example.BoardProject_back.repository.GradeRepository;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;


    /** 회원가입 로직 */
    @Override
    public void account_creative(CreateDTO createDTO) {
        GradeEntity gradeEntity = gradeRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 인수 들어와있슴!!!"));

        UserEntity userEntity = UserEntity.builder()
                .email(createDTO.getEmail())
                .grade(gradeEntity)
                .password(createDTO.getPassword())
                .nickName(createDTO.getNickName())
                .userRole(UserRole.USER)
                .build();
        userRepository.save(userEntity);
    }
}
