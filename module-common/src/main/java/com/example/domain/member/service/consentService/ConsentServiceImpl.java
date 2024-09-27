package com.example.domain.member.service.consentService;

import com.example.domain.member.entity.MarketingConsent;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.SystemConsent;
import com.example.domain.member.repository.MarketingConsentRepository;
import com.example.domain.member.repository.SystemConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConsentServiceImpl implements ConsentService{

    private final MarketingConsentRepository marketingConsentRepository;
    private final SystemConsentRepository systemConsentRepository;

    @Override
    public void saveMarketingConsent(Member member, Boolean isAgreed) {
            marketingConsentRepository.save(
                    MarketingConsent.builder()
                            .member(member)
                            .isAgreed(isAgreed)
                            .date(LocalDate.now())
                            .build()
            );
    }

    @Override
    public void saveSystemConsent(Member member, Boolean isAgreed) {
            systemConsentRepository.save(
                    SystemConsent.builder()
                            .member(member)
                            .isAgreed(isAgreed)
                            .date(LocalDate.now())
                            .build()
            );
    }
}
