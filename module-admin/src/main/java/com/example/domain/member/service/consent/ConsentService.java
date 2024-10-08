package com.example.domain.member.service.consent;

import com.example.domain.member.entity.FCMToken;
import com.example.domain.member.entity.MarketingConsent;

import java.util.List;

public interface ConsentService {

    public List<Long> findAllMarketingAgreedMember();

    public boolean checkAgreedMember(Long memberId);
}
