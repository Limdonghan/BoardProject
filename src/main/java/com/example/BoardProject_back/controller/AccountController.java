package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.*;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.AccountService;
import com.example.BoardProject_back.service.AccountSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AccountController {
    private final AccountService accountService;
    private final AccountSettingService accountSettingService;
    private final PasswordEncoder passwordEncoder;


    /// 회원가입
    @PostMapping("/createAccount")
    @ResponseStatus(HttpStatus.CREATED)  //회원가입메서드 이니깐 status = 201
    public ResponseEntity signup(@Validated @RequestBody CreateDTO createDTO) {
        String encode = passwordEncoder.encode(createDTO.getPassword());
        createDTO.setPassword(encode);
        accountService.accountCreative(createDTO);
        return ResponseEntity.ok("회원가입성공하냐??!");
    }

    /// 유저 정보 검색
    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getCurrentUserInfo() {
        return ResponseEntity.ok(accountService.getCurrentUserInfo());
    }

    /// AccessToken 재생성
    @PostMapping("/refresh")
    public JwtTokenDTO refresh(@Validated @RequestBody RefreshTokenDTO refreshTokenDTO) {
        return accountService.getRefreshToken(refreshTokenDTO.getRefreshToken());
    }


    /// 유저 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity modifyUserInfo(@Validated @RequestBody UserUpdateProfileDTO userUpdateProfileDTO,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        accountSettingService.updateProfile(userUpdateProfileDTO, customUserDetails.getUserEntity());
        return ResponseEntity.ok("닉네임 변경 완료");
    }

    /// 유저 비밀번호 수정
    @PatchMapping("password")
    public ResponseEntity modifyUserPassword(@Validated @RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        accountSettingService.updatePassword(userUpdatePasswordDTO, customUserDetails.getUserEntity());
        return ResponseEntity.ok("비밀번호 변경 완료");
    }

}
