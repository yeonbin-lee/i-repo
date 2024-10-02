package com.example.domain.member.service.fcm;

import com.example.domain.member.entity.FCMToken;
import com.example.domain.member.entity.Member;
import com.example.domain.member.repository.FcmRepository;
import com.example.domain.member.service.memberService.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService{

    private final FcmRepository fcmRepository;

    @Override
    @CachePut(value = "marketingConsent", key = "#member.id")
    public String updateFCMToken(Member member, String newFcmToken) {

        FCMToken existingFcmToken = fcmRepository.findByMemberId(member.getId());
        System.out.println("member.id=" + member.getId());
        // 기존에 존재하는 경우 업데이트
        if (existingFcmToken != null) {
            existingFcmToken.setToken(newFcmToken);
            fcmRepository.save(existingFcmToken);
        } else { // 존재하지 않는 경우 생성
            fcmRepository.save(FCMToken.builder()
                    .member(member)
                    .token(newFcmToken)
                    .createdAt(LocalDate.now())
                    .build()
            );
        }
        // Redis에 저장
        return newFcmToken;
    }
}
