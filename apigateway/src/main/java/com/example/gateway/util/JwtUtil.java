package com.example.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${spring.jwt.secret}")
    private String secret;


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


    // 토큰에서 클레임 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) { // Access Token
            return e.getClaims();
        }
    }

    public String getRole(String token){
        Claims claims = getClaims(token);
        return claims.get("role").toString();
    }

    // 토큰에서 사용자 이름 추출
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 토큰에서 만료 날짜 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 토큰이 만료되었는지 확인
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰 검증
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    // 토큰에서 모든 클레임을 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 역할(Role) 추출
    public String extractRole(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        return role;
    }
}
