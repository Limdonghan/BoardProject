package com.example.BoardProject_back.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.security.jwt")  //application 경로 저장
public class JwtProperties {

    private String secretKey;  /// JWT 비밀키를 저장

    private long expiration; /// JWT 만료시간을 저장

    private long refreshExpiration;  /// Refresh Token의 만료시간 저장
}
