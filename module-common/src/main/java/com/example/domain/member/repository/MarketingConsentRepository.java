package com.example.domain.member.repository;

import com.example.domain.member.entity.MarketingConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketingConsentRepository extends JpaRepository<MarketingConsent, Long> {
}
