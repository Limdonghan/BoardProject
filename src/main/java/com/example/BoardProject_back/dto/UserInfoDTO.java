package com.example.BoardProject_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDTO {

    @Email
    @NotNull
    private final String email;

    @NotNull
    private final String username;

    @NotNull
    private final String grade;

    @NotNull
    private final int points;

    @NotNull
    private final String role;

}
