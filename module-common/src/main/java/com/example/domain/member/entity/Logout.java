package com.example.domain.member.entity;

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
@RedisHash("logout")
//@NoArgsConstructor
public class Logout {

    @Id
    private String id; // access_token

    private String data;

    @TimeToLive(unit= TimeUnit.SECONDS)
    private Integer expiration;
}
