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


    /**
     * 인증번호 전송 API
     * @param request - phone 사용자 입력
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendSMS(@RequestBody @Valid SmsRequest request){
        smsService.sendSms(request);
        return ResponseEntity.ok("문자를 전송했습니다.");
    }

    /** 비용 문제로 만든 Redis 저장 인증코드*/
    @PostMapping("/fake/send")
    public ResponseEntity<?> fakeSendSMS(@RequestBody @Valid SmsRequest request){
        smsService.fakeSendSms(request);
        return ResponseEntity.ok("문자를 전송했습니다.");
    }

    /**
     * 인증코드 확인
     * @param request - phone
     * @param request - code 사용자 입력
     * @return boolean (true - 일치, false - 불일치)
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifySMS(@RequestBody @Valid SmsVerifyRequest request){
        return new ResponseEntity<>(smsService.verifySms(request), HttpStatus.OK);
    }

}