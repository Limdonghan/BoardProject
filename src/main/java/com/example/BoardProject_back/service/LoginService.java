package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.JwtTokenDTO;
import com.example.BoardProject_back.dto.LoginDTO;

public interface LoginService {
    JwtTokenDTO authLogin(LoginDTO loginDTO);
}
