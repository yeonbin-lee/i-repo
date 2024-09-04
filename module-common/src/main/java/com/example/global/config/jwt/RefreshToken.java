package com.example.global.config.jwt;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Setter
@Getter
@AllArgsConstructor
@Builder
@RedisHash("refresh")
public class RefreshToken {

    @Id
    private String id;

    private String refresh_token;

    @TimeToLive(unit = TimeUnit.DAYS)
    private Integer expiration;
}
