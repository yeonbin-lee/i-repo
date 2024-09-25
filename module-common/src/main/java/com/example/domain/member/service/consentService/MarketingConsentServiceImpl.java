package com.example.domain.member.service.consentService;

import com.example.domain.member.entity.MarketingConsent;
import com.example.domain.member.repository.MarketingConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketingConsentServiceImpl implements MarketingConsentService {

    private final MarketingConsentRepository marketingConsentRepository;


    public void saveMarketingConsent(MarketingConsent marketingConsent) {
        marketingConsentRepository.save(marketingConsent);
    }

}
