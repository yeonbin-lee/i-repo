package com.example.domain.coolsms.service;

import com.example.domain.coolsms.controller.dto.request.SmsRequest;
import com.example.domain.coolsms.controller.dto.request.SmsVerifyRequest;
import com.example.domain.coolsms.entity.Sms;
import com.example.domain.coolsms.repository.SmsRepository;
import com.example.domain.coolsms.utils.SmsCertificationUtil;
import com.example.global.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final SmsCertificationUtil smsCertificationUtil;
    private final SmsRepository smsRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override // SmsService 인터페이스 메서드 구현
    public void sendSms(SmsRequest request) {
        String phone = request.getPhone(); // SmsrequestDto에서 전화번호를 가져온다.

        String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000); // 6자리 인증 코드를 랜덤으로 생성
        smsCertificationUtil.sendSMS(phone, String.valueOf(certificationCode)); // SMS 인증 유틸리티를 사용하여 SMS 발송

        // 이미 인증코드를 발급받았다면 해당 전화번호의 인증코드를 삭제한다.
        if (existsByPhone(phone)){
            deleteByPhone(phone);
        }

        // 인증코드 저장
        saveSms(
                Sms.builder()
                        .id(phone)
                        .code(certificationCode)
                        .expiration(180) // 180 = 60 * 3 (5분)
                        .build()
        );

    }


    @Override // SmsService 인터페이스 메서드 구현
    public void fakeSendSms(SmsRequest request) {
        String phone = request.getPhone(); // SmsrequestDto에서 전화번호를 가져온다.

        String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000); // 6자리 인증 코드를 랜덤으로 생성
        System.out.println("code = " + certificationCode);
        // 이미 인증코드를 발급받았다면 해당 전화번호의 인증코드를 삭제한다.
        if (existsByPhone(phone)){
            deleteByPhone(phone);
        }

        // 인증코드 저장
        saveSms(
                Sms.builder()
                        .id(phone)
                        .code(certificationCode)
                        .expiration(180) // 180 = 60 * 3 (5분)
                        .build()
        );
    }


    @Override
    public boolean verifySms(SmsVerifyRequest request){
        String phone = request.getPhone();
        Sms sms =  findSmsByPhone(phone);
        if(sms.getCode().equals(request.getCertificationCode())){
            deleteByPhone(phone);
            verifyPhone(phone);
            return true;
        }
        return false;
    }

    public Sms findSmsByPhone(String phone) {
        return smsRepository.findById(phone).orElseThrow(
                () -> new UserNotFoundException("등록되지 않은 전화번호입니다.")
        );
    }

    public void deletePhone(String phone){
        smsRepository.deleteById(phone);
    }


    public boolean existsByPhone(String phone){
        return smsRepository.existsById(phone);
    }

    public void deleteByPhone(String phone) {
        smsRepository.deleteById(phone);
    }

    public void saveSms(Sms sms) {
        smsRepository.save(sms);
    }

    public void verifyPhone(String phone) {
        // Redis에 "verified_phone:{phone}" 형식으로 저장
        String redisKey = "verified_phone:" + phone;

        // 로그아웃 상태를 기록 (true로 설정)
        redisTemplate.opsForValue().set(redisKey, "verify");

        // 1시간 동안 유효 (예: 로그아웃 상태를 1시간 동안 유지)
        redisTemplate.expire(redisKey, Duration.ofMinutes(10));
    }
}
