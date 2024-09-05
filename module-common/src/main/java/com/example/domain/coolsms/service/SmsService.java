package com.example.domain.coolsms.service;

import com.example.domain.coolsms.controller.dto.request.SmsRequest;
import com.example.domain.coolsms.controller.dto.request.SmsVerifyRequest;
import com.example.domain.coolsms.entity.Sms;

public interface SmsService {

    public void sendSms(SmsRequest smsRequest);
    public void fakeSendSms(SmsRequest smsRequest);
    public boolean verifySms(SmsVerifyRequest smsVerifyRequest);
    public void deletePhone(String phone);
    public Sms findSmsByPhone(String phone);
}
