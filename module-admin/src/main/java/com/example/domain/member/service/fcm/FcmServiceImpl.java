package com.example.domain.member.service.fcm;

import com.example.domain.member.controller.dto.request.MessagePushRequest;
import com.example.domain.member.repository.FCMTokenRepository;
import com.example.domain.member.service.consent.ConsentService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService{

    private final ConsentService consentService;
    private final FCMTokenRepository fcmTokenRepository;
//    private final FirebaseMessaging firebaseMessaging;


    // (마케팅 수신동의한 멤버) 전체 알림 전송
    public void sendBulkMarketingNotification(MessagePushRequest request) {
        List<Long> agreedMembers = consentService.findAllMarketingAgreedMember();
        List<String> targetTokens = fcmTokenRepository.findTokensByMemberIds(agreedMembers);

        // 토큰이 없을 경우 처리
        if (targetTokens.isEmpty()) {
            System.out.println("No target tokens found for marketing agreed members.");
            return; // 토큰이 없으면 조기 리턴
        }


        // Notification 객체를 Builder를 통해 생성
        Notification notification = Notification.builder()
                .setTitle(request.title())
                .setBody(request.body())
                .build();

        // MulticastMessage를 만들기 위해 토큰을 500개씩 나누기
        int batchSize = 500;

        for (int i = 0; i < targetTokens.size(); i += batchSize) {
            // 500개씩 서브리스트 생성
            List<String> tokensBatch = targetTokens.subList(i, Math.min(i + batchSize, targetTokens.size()));
            // MulticastMessage 객체 생성
            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .setNotification(notification)
                    .addAllTokens(tokensBatch)
                    .build();

            // 비동기 처리
            CompletableFuture.runAsync(() -> {
                try {
                    // FCM 메시지 전송
                    BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(multicastMessage);
                    System.out.println(response.getSuccessCount() + " messages were sent successfully");
                    System.out.println(response.getFailureCount() + " messages failed to send.");
                } catch (FirebaseMessagingException e) {
                    System.out.println("Error sending FCM message: " + e.getMessage());
                }
            });
        }
    }
}
