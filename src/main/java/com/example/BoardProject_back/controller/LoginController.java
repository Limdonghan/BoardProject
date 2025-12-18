package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.JwtTokenDTO;
import com.example.BoardProject_back.dto.LoginDTO;
import com.example.BoardProject_back.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "유저 로그인 API")
    public JwtTokenDTO login(@RequestBody LoginDTO loginDTO) {
        return loginService.authLogin(loginDTO);
    }


    @PostMapping("/logOut")
    @Operation(summary = "로그아웃", description = "유저 로그아웃 API")
    public ResponseEntity logout(@RequestBody JwtTokenDTO jwtTokenDTO) {
        loginService.logout(jwtTokenDTO.getAccessToken());
        return ResponseEntity.ok("logout success");

    }

}
