package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.*;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.AccountService;
import com.example.BoardProject_back.service.AccountSettingService;
import io.swagger.v3.oas.annotations.Operation;
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


    @PostMapping("/createAccount")
    @Operation(summary = "회원가입", description = "회원가입 API")
    @ResponseStatus(HttpStatus.CREATED)  //회원가입메서드 이니깐 status = 201
    public ResponseEntity signup(@Validated @RequestBody CreateDTO createDTO) {
        String encode = passwordEncoder.encode(createDTO.getPassword());
        createDTO.setPassword(encode);
        accountService.accountCreative(createDTO);
        return ResponseEntity.ok("회원가입성공하냐??!");
    }

    @GetMapping("/me")
    @Operation(summary = "정보 검색", description = "유저(본인) 정보 검색 API")
    public ResponseEntity<UserInfoDTO> getCurrentUserInfo() {
        return ResponseEntity.ok(accountService.getCurrentUserInfo());
    }

    @PostMapping("/refresh")
    @Operation(summary = "AccessToken 재생성", description = "AccessToken만료시 재생성 API")
    public JwtTokenDTO refresh(@Validated @RequestBody RefreshTokenDTO refreshTokenDTO) {
        return accountService.getRefreshToken(refreshTokenDTO.getRefreshToken());
    }

    @PatchMapping("/profile")
    @Operation(summary = "프로필 수정", description = "유저(본인) 프로필 수정 API")
    public ResponseEntity modifyUserInfo(@Validated @RequestBody UserUpdateProfileDTO userUpdateProfileDTO,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        accountSettingService.updateProfile(userUpdateProfileDTO, customUserDetails.getUserEntity());
        return ResponseEntity.ok("닉네임 변경 완료");
    }

    @PatchMapping("password")
    @Operation(summary = "비밀번호 수정", description = "유저(본인) 비밀번호 수정 API")
    public ResponseEntity modifyUserPassword(@Validated @RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        accountSettingService.updatePassword(userUpdatePasswordDTO, customUserDetails.getUserEntity());
        return ResponseEntity.ok("비밀번호 변경 완료");
    }


    /// 유저 닉네임 중복체크
    /// GET /api/account/check-nickname?nickname=사용자입력값
    @GetMapping("/check-nickname")
    @Operation(summary = "닉제임 중복 체크", description = "닉네임 중복 체크 API")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate =  accountSettingService.isNicknameDuplicate(nickname);

        /// 중복이면 true, 사용 가능하면 false 반환
        return ResponseEntity.ok(isDuplicate);
    }

}
