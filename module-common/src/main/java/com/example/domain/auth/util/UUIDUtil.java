package com.example.domain.auth.util;

import java.util.UUID;

public class UUIDUtil {

    // 4자리 UUID 생성 메서드
    public static String generate4CharUUID() {
        // UUID 생성
        String uuid = UUID.randomUUID().toString();
        // 4자리만 추출
        return uuid.substring(0, 4);
    }
}
