package com.example.domain.member.service.terms;

import com.example.domain.member.entity.TermsCondition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service

public class MemberTermsAgreementServiceImpl implements MemberTermsAgreementService{

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void updateAllMembersToAgreeToTerms(TermsCondition termsCondition){
        // Native SQL 쿼리 작성
        String query = "INSERT INTO member_terms_agreement (member_id, terms_condition_id, is_agreed, agreed_at) " +
                "SELECT m.member_id, :termsConditionId, true, CURRENT_DATE " +
                "FROM member m " +
                "ON CONFLICT (member_id, terms_condition_id) " +
                "DO UPDATE SET is_agreed = true, agreed_at = CURRENT_DATE";

        entityManager.createNativeQuery(query)
                .setParameter("termsConditionId", termsCondition.getId())
                .executeUpdate();
    }

}
