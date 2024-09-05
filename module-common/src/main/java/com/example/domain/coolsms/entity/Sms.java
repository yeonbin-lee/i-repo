package com.example.domain.coolsms.entity;

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
@RedisHash("sms")
//@NoArgsConstructor
public class Sms {

    @Id
    private String id;

    private String code;

    @TimeToLive(unit= TimeUnit.SECONDS)
    private Integer expiration;
}
