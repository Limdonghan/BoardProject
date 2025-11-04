package com.example.BoardProject_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDTO {

    @NotNull
    @Email
    private String email;  // 로그인 ID 및 Email

    @NotNull
    private String password;

    @NotNull
    private String nickName;

    @NotNull
    private int grade;  // 등급
}
