package com.example.domain.member.service.terms;

import com.example.domain.member.entity.MemberTermsAgreement;
import com.example.domain.member.repository.MemberTermsAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberTermsAgreementServiceImpl implements MemberTermsAgreementService{

    private final MemberTermsAgreementRepository memberTermsAgreementRepository;

    @Override
    public void saveMemberTermsAgreement(MemberTermsAgreement memberTermsAgreement) {
        memberTermsAgreementRepository.save(memberTermsAgreement);

    }

}
