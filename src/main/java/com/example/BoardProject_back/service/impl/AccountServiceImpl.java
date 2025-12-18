package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.CreateDTO;
import com.example.BoardProject_back.dto.JwtTokenDTO;
import com.example.BoardProject_back.dto.UserInfoDTO;
import com.example.BoardProject_back.entity.GradeEntity;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.entity.UserRole;
import com.example.BoardProject_back.jwt.JwtProvider;
import com.example.BoardProject_back.repository.GradeRepository;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.service.AccountService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 회원가입 로직
     */
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


    /**
     * 회원 정보 검색
     */
    @Override
    public UserInfoDTO getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("사용자를 찾을 수 없음!@!@");
        }

        String userid = authentication.getName();

        UserEntity userEntity = userRepository.findByEmail(userid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음222222"));

        String grade = userEntity.getGrade().getGrade();
        return UserInfoDTO.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getNickName())
                .grade(grade)
                .points(userEntity.getPoint())
                .role(userEntity.getUserRole().toString())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }

    /**
     * AccessToken 만료 RefreshToken으로 AccessToken 재발급
     */
    @Override
    public JwtTokenDTO getRefreshToken(String refreshToken) {
        Jws<Claims> claims = jwtProvider.getClaims(jwtProvider.extractToken(refreshToken));  /// 토큰 비어있는지 확인
        if (jwtProvider.isWrongType(claims, "Refresh")) {
            throw new IllegalStateException("잘못된 토큰! 아직 AccessToken 살아있음");
        }
        String redisRefreshToken = redisTemplate.opsForValue().get(claims.getBody().getSubject());
        if (!redisRefreshToken.equals(refreshToken)) {
            throw new IllegalStateException("RefreshToken이 일치하지 않음");
        }

        String accessToken = jwtProvider.createAccessToken(claims.getBody().getSubject());

        return JwtTokenDTO.builder()
                .loginMessage("AccessToken재생성완료")
                .AccessToken(accessToken)
                .build();
    }




}
