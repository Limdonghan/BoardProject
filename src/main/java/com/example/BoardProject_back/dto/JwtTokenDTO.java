package com.example.BoardProject_back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JwtTokenDTO {
    private String loginMessage;
    private String AccessToken;
    private String RefreshToken;
}
