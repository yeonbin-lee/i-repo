package com.example.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${spring.jwt.secret}")
    private String secretKey;


    /**
     * 로그아웃 로직
     * */
    public boolean isTokenLogout(String token) {
        // Redis에 해당 토큰이 존재하는지 확인
        return Boolean.TRUE.equals(redisTemplate.hasKey(getRedisKeyForToken(token)));
    }

    private String getRedisKeyForToken(String token) {
        return "logout:" + token;
    }
    /***/




    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
