package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.JwtTokenDTO;
import com.example.BoardProject_back.dto.LoginDTO;
import com.example.BoardProject_back.jwt.JwtProvider;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.LoginService;
import com.example.BoardProject_back.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtTokenDTO authLogin(LoginDTO loginDTO) {
        log.info("UserPasswordAuthenticationToken 발급");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if(!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않음");
        }

        return JwtTokenDTO.builder()
                .loginMessage("로그인 성공!?!?!")
                .AccessToken(jwtProvider.createAccessToken(userDetails.getUserEntity().getEmail()))
                .RefreshToken(jwtProvider.createRefreshToken(userDetails.getUserEntity().getEmail()))
                .build();
    }

    @Override
    @Transactional
    public void logout(String accessToken) {
        Jws<Claims> claims = jwtProvider.getClaims(accessToken);

        /// AccessToken 유효시간 저장
        Long expiration = jwtProvider.getExpiration(accessToken);

        /// Refresh Token 삭제
        redisService.refreshTokenDelete(claims.getBody().getSubject());

        ///  Access Token 블랙리스트 등록
        redisService.setValuesWithTimeout(accessToken,"logout",expiration);



    }
}
