package com.example.BoardProject_back.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 10)
public class RefreshTokenDTO {

    private String refreshToken;

}
