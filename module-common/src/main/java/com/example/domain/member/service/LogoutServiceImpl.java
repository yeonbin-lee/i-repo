package com.example.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void logoutUser(String accessToken, Integer sec) {
        // Redis에 "logout:email" 형식으로 저장
        String redisKey = "logout:" + accessToken;

        // 로그아웃 상태를 기록 (true로 설정)
        redisTemplate.opsForValue().set(redisKey, "logout");

        // 1시간 동안 유효 (예: 로그아웃 상태를 1시간 동안 유지)
        redisTemplate.expire(redisKey, Duration.ofSeconds(sec));
    }
}
