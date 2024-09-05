package com.example.domain.coolsms.service;

import com.example.domain.coolsms.controller.dto.request.SmsRequest;
import com.example.domain.coolsms.controller.dto.request.SmsVerifyRequest;
import com.example.domain.coolsms.entity.Sms;
import com.example.domain.coolsms.repository.SmsRepository;
import com.example.domain.coolsms.utils.SmsCertificationUtil;
import com.example.global.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final SmsCertificationUtil smsCertificationUtil;
    private final SmsRepository smsRepository;

    @Override // SmsService 인터페이스 메서드 구현
    public void sendSms(SmsRequest smsRequest) {
        String phoneNum = smsRequest.getPhoneNum(); // SmsrequestDto에서 전화번호를 가져온다.

        String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000); // 6자리 인증 코드를 랜덤으로 생성
        smsCertificationUtil.sendSMS(phoneNum, String.valueOf(certificationCode)); // SMS 인증 유틸리티를 사용하여 SMS 발송

        // 이미 인증코드를 발급받았다면 해당 전화번호의 인증코드를 삭제한다.
        if (smsRepository.existsById(phoneNum)){
            smsRepository.deleteById(phoneNum);
        }

        // 인증코드 저장
        smsRepository.save(
                Sms.builder()
                        .id(phoneNum)
                        .code(certificationCode)
                        .expiration(180) // 180 = 60 * 3 (5분)
                        .build()
        );
    }


    @Override // SmsService 인터페이스 메서드 구현
    public void fakeSendSms(SmsRequest smsRequest) {
        String phoneNum = smsRequest.getPhoneNum(); // SmsrequestDto에서 전화번호를 가져온다.

        String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000); // 6자리 인증 코드를 랜덤으로 생성
        System.out.println("code = " + certificationCode);
        // 이미 인증코드를 발급받았다면 해당 전화번호의 인증코드를 삭제한다.
        if (smsRepository.existsById(phoneNum)){
            smsRepository.deleteById(phoneNum);
        }

        // 인증코드 저장
        smsRepository.save(
                Sms.builder()
                        .id(phoneNum)
                        .code(certificationCode)
                        .expiration(180) // 180 = 60 * 3 (5분)
                        .build()
        );
    }


    @Override
    public boolean verifySms(SmsVerifyRequest smsVerifyRequest){
        Sms sms = smsRepository.findById(smsVerifyRequest.getPhoneNum()).get();
        if(sms.getCode().equals(smsVerifyRequest.getCertificationCode())){
//            smsRepository.deleteById(smsVerifyRequest.getPhoneNum());
            return true;
        }
        return false;
    }

    public void deletePhone(String phone){
        smsRepository.deleteById(phone);
    }

    public Sms findSmsByPhone(String phone) {
        Sms sms = smsRepository.findById(phone).orElseThrow(
                () -> new UserNotFoundException("등록되지 않은 전화번호입니다.")
        );
        return sms;
    }
}
