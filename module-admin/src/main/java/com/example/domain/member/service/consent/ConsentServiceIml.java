package com.example.domain.member.service.consent;

import com.example.domain.member.entity.MarketingConsent;
import com.example.domain.member.repository.MarketingConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsentServiceIml implements ConsentService{

    private final MarketingConsentRepository marketingConsentRepository;

    @Cacheable(value = "agreedMembersCache")
    public List<Long> findAllMarketingAgreedMember(){
        List<Long> agreedMember = marketingConsentRepository.findLatestAgreedMembers();
        return agreedMember;
    }

    public boolean checkAgreedMember(Long memberId) {
        return marketingConsentRepository.existsByMemberId(memberId);
    }

}
