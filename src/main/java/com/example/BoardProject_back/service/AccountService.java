package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.CreateDTO;
import com.example.BoardProject_back.dto.JwtTokenDTO;
import com.example.BoardProject_back.dto.UserInfoDTO;

public interface AccountService {
    void accountCreative(CreateDTO createDTO);  // 유저 회원가입

    UserInfoDTO getCurrentUserInfo();  // 유저 정보 검색

    JwtTokenDTO getRefreshToken(String token);  // AccessToken 재생성


}
