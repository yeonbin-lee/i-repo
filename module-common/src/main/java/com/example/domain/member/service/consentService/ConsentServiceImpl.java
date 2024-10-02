package com.example.domain.member.service.consentService;

import com.example.domain.member.entity.MarketingConsent;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.SystemConsent;
import com.example.domain.member.repository.MarketingConsentRepository;
import com.example.domain.member.repository.SystemConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConsentServiceImpl implements ConsentService{

    private final MarketingConsentRepository marketingConsentRepository;
    private final SystemConsentRepository systemConsentRepository;


    @Override
    @CacheEvict(value = "agreedMembersCache", allEntries = true)
    public Long saveMarketingConsent(Member member, Boolean isAgreed) {
        marketingConsentRepository.save(
                MarketingConsent.builder()
                        .member(member)
                        .isAgreed(isAgreed)
                        .date(LocalDate.now())
                        .build()
        );
        return member.getId();
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
