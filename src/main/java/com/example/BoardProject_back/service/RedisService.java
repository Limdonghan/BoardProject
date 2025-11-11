package com.example.BoardProject_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisService {

    @Transactional
    public void setValuesWithTimeout (RedisTemplate<String, String> redisTemplate, String key, int timeout) {
        redisTemplate.opsForValue().set(key, String.valueOf(timeout));
    }
}
