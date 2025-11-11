package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.CreateDTO;
import com.example.BoardProject_back.dto.UserInfoDTO;
import com.example.BoardProject_back.entity.GradeEntity;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.entity.UserRole;
import com.example.BoardProject_back.repository.GradeRepository;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;


    /** 회원가입 로직 */
    @Override
    public void accountCreative(CreateDTO createDTO) {
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


    /** 회원 정보 검색 */
    @Override
    public UserInfoDTO getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("사용자를 찾을 수 없음!@!@");
        }

        String userid = authentication.getName();
        log.info("userid: {}", userid);

        UserEntity userEntity = userRepository.findByEmail(userid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음222222"));

        /*
        * 유저 회원 정보 찾기에서 회원 등급을 가져와야하는데
        * 이 회원 등급은 Grade테이블에 존재함 1(유저):N(등급)으로 구성되어 있는데...
        * grade 등급을 가져와서 실제 등급내용을 표시하고 싶으면은....
        *
        * 근데 grade는 주소 값이 나오고 (해결) password는 왜 나오는겨(해결 DTO에 password값이 있었음)?
        * */
        String grade = userEntity.getGrade().getGrade();
        return UserInfoDTO.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getNickName())
                .grade(grade)
                .points(userEntity.getPoint())
                .role(userEntity.getUserRole().toString())
                .build();
    }






}
