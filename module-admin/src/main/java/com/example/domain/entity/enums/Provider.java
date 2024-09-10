package com.example.domain.entity.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    NORMAL("normal"),
    KAKAO("kakao"),
    GOOGLE("google");

    private final String providerName;
}