package com.example.domain.member.controller.dto.request;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record MessagePushRequest(
        String title,
        String body
) {

    public static MessagePushRequest of(String title, String body) {
        return MessagePushRequest.builder()
                .title(title)
                .body(body)
                .build();
    }
}

