package com.example.domain.member.service.consent;

import com.example.domain.member.entity.MarketingConsent;
import com.example.domain.member.repository.MarketingConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsentServiceIml implements ConsentService{

    private final MarketingConsentRepository marketingConsentRepository;

//    @Override
//    public List<Long> getMarketingConsentMembers() {
//        List<MarketingConsent> mostRecentAgreedConsents = marketingConsentRepository.findMostRecentAgreedConsents();
//        List<Long> consentIds = mostRecentAgreedConsents.stream()
//                .map(MarketingConsent::getId)  // 각 MarketingConsent 객체의 id 값을 가져옴
//                .collect(Collectors.toList());
//
//        return consentIds;  // id 리스트 반환
//    }
//
    @Override
    public List<String> getMarketingConsentFCMTokenList() {
        List<String> tokenList = marketingConsentRepository.findMostRecentAgreedConsents1();

        return tokenList;  // FCMToken 리스트 반환
    }

}
