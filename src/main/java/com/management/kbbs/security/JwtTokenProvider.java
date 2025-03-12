package com.management.kbbs.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private long validityInMilliseconds = 3600000; // 1 hour

    // 生成 JWT token
    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role); // 添加角色訊息

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 從 JWT token 提取用戶名稱
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 驗證 JWT token 是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 獲得 token 對應用戶的權限
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(secretKey)
                            .parseClaimsJws(token)
                            .getBody();
        String role = claims.get("role", String.class);
        if ("MEMBER".equals(role)) {
            return "ROLE_MEMBER";
        } else if ("ADMIN".equals(role)) {
            return "ROLE_ADMIN";
        }
        return null;
    }
}