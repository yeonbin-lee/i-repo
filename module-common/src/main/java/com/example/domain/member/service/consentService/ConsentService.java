package com.example.domain.member.service.consentService;

import com.example.domain.member.entity.Member;

public interface ConsentService {

    public void saveMarketingConsent(Member member, Boolean isAgreed);
    public void saveSystemConsent(Member member, Boolean isAgreed);
}
