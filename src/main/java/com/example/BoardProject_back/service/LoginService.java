package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.JwtTokenDTO;
import com.example.BoardProject_back.dto.LoginDTO;
import org.springframework.transaction.annotation.Transactional;

public interface LoginService {
    JwtTokenDTO authLogin(LoginDTO loginDTO);  // 로그인

    @Transactional
    void logout(String accessToken);  // 로그아웃
}
