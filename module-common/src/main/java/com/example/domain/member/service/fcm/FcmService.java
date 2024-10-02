package com.example.domain.member.service.fcm;

import com.example.domain.member.entity.Member;

public interface FcmService {

//    public void updateFCMToken(Member member, String newFcmToken);
    public String updateFCMToken(Member member, String newFcmToken);
}
