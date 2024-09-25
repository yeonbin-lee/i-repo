package com.example.domain.member.service;

import com.example.domain.member.entity.TermsCondition;

public interface MemberTermsAgreementService {
    public void updateAllMembersToAgreeToTerms(TermsCondition termsConditon);
}
