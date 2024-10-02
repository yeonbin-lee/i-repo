package com.example.domain.member.service.consent;

import com.example.domain.member.entity.MarketingConsent;
import com.example.domain.member.repository.MarketingConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsentServiceIml implements ConsentService{

    private final MarketingConsentRepository marketingConsentRepository;
    // 전체 알림 전송 메서드
//    @Cacheable(value = "marketingMembers")
//    public List<String> sendNotificationToAll(String message) {
//        List<String> fcmTokens = marketingConsentRepository.findAllFCMTokens();
//
//        /** 전송 로직 필요 */
////        for (String token : fcmTokens) {
////
////            sendNotification(token, message);
////        }
//        return fcmTokens;
//    }

    @Cacheable(value = "agreedMembersCache")
    public List<Long> findAllAgreedMember(){
        List<Long> agreedMember = marketingConsentRepository.findLatestAgreedMembers();
        return agreedMember;
    }




//    // 개별 알림 전송 메서드
//    @Cacheable(value = "marketingConsent", key = "#memberId")
//    public String sendNotification(Long memberId, String message) {
//        String fcmToken = marketingConsentRepository.findFCMToken(memberId).orElseThrow(
//                () -> new IllegalArgumentException("FCMToken 값 없음")
//        );
//
//        /** 전송 로직 필요 */
//        System.out.println("Sending notification to " + memberId + ": " + message);
//        return fcmToken;
//    }

//    @Override
//    @Cacheable(value = "marketingConsentCache")
//    public List<String> getMarketingConsentFCMTokenList() {
//        long startTime = System.currentTimeMillis();
//        List<String> tokenList = marketingConsentRepository.findMostRecentAgreedConsents1();
//
//        // 종료 시간 측정
//        long endTime = System.currentTimeMillis();
//
//        // 소요 시간 계산
//        long elapsedTime = endTime - startTime;
//
//        System.out.println("소요 시간: " + elapsedTime + " 밀리초");
//
//        return tokenList;  // FCMToken 리스트 반환
//
//    }

}
