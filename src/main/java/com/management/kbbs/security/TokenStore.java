package com.management.kbbs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    // 存儲 JWT token
    public void storeToken(String username, String token) {
        redisTemplate.opsForValue().set(username, token);
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