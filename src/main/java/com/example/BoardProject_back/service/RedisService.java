package com.example.BoardProject_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * AccessToken redis 에 저장
     */
    @Transactional
    public void setValuesWithTimeout(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(
                key,
                value,
                timeout,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * RefreshToken 삭제
     */
    @Transactional
    public void refreshTokenDelete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Redis에 해당 키(블랙리스트)가 존재하는지 안전하게 확인
     */
    public boolean keyBlackListCheck(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
