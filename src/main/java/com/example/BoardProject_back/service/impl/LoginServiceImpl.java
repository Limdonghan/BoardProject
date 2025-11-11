package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.JwtTokenDTO;
import com.example.BoardProject_back.dto.LoginDTO;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.jwt.JwtProvider;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtTokenDTO authLogin(LoginDTO loginDTO) {
        log.info("UserPasswordAuthenticationToken 발급");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return JwtTokenDTO.builder()
                .loginMessage("로그인 성공!?!?!")
                .AccessToken(jwtProvider.createAccessToken(userDetails.getUserEntity().getEmail()))
                .RefreshToken(jwtProvider.createRefreshToken(userDetails.getUserEntity().getEmail()))
                .build();
    }
}
