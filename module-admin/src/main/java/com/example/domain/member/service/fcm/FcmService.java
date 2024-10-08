package com.example.domain.member.service.fcm;

import com.example.domain.member.controller.dto.request.MessagePushRequest;

import java.util.List;

public interface FcmService {
    public void sendBulkMarketingNotification(MessagePushRequest request);
}
