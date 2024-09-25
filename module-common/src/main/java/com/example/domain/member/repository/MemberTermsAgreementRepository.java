package com.example.domain.member.repository;

import com.example.domain.member.entity.MemberTermsAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTermsAgreementRepository extends JpaRepository<MemberTermsAgreement, Long> {
}
