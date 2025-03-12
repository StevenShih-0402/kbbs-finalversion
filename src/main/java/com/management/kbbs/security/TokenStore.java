package com.management.kbbs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    // 存儲 JWT token
    public void storeToken(String username, String token, Long expirationTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(username, token, expirationTime, timeUnit);
    }

    // 獲取 JWT token
    public String getToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    // 刪除 JWT token
    public void removeToken(String username) {
        redisTemplate.delete(username);
    }
}