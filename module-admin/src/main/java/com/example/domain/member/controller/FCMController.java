package com.example.domain.member.controller;

import com.example.domain.member.controller.dto.request.MessagePushRequest;
import com.example.domain.member.service.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/module-admin/fcm")
public class FCMController {

    private final FcmService fcmService;

    @PostMapping("/marketing/send-bulk")
    public ResponseEntity<String> findFCMTokenAboutAgreedMembers(@RequestBody MessagePushRequest request) {
        log.info("[+] 푸시 메시지를 전송합니다. ");
        fcmService.sendBulkMarketingNotification(request);
        return ResponseEntity.ok("Notifications sent successfully.");
    }


//    @PostMapping("/send/all")
//    public ResponseEntity<?> findFCMTokenAboutAgreedMembers(@RequestBody FcmSendDto fcmSendDto) {
//        log.info("[+] 푸시 메시지를 전송합니다. ");
//        List<String> fcmTokenAboutAgreedMembers = fcmService.findFCMTokenAboutAgreedMembers();
//        return ResponseEntity.status(HttpStatus.OK).body(fcmTokenAboutAgreedMembers);
//    }
}
