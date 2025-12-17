package com.example.BoardProject_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotNull
    private String login;   //로그인 ID

    @NotNull
    private String password; //사용자 비밀번호
}
