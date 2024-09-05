package com.example.domain.coolsms.controller;

import com.example.domain.coolsms.controller.dto.request.SmsRequest;
import com.example.domain.coolsms.controller.dto.request.SmsVerifyRequest;
import com.example.domain.coolsms.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;


    @PostMapping("/send")
    public ResponseEntity<?> sendSMS(@RequestBody @Valid SmsRequest smsRequest){
        smsService.sendSms(smsRequest);
        return ResponseEntity.ok("문자를 전송했습니다.");
    }

    /** 비용 문제로 만든 Redis 저장 인증코드*/
    @PostMapping("/fake/send")
    public ResponseEntity<?> fakeSendSMS(@RequestBody @Valid SmsRequest smsRequest){
        smsService.fakeSendSms(smsRequest);
        return ResponseEntity.ok("문자를 전송했습니다.");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifySMS(@RequestBody @Valid SmsVerifyRequest smsVerifyRequest){
        return new ResponseEntity<>(smsService.verifySms(smsVerifyRequest), HttpStatus.OK);
    }

}