package com.example.BoardProject_back.controller;

import com.example.BoardProject_back.dto.JwtTokenDTO;
import com.example.BoardProject_back.dto.LoginDTO;
import com.example.BoardProject_back.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public JwtTokenDTO login(@RequestBody LoginDTO loginDTO) {
        return loginService.authLogin(loginDTO);
    }
}
