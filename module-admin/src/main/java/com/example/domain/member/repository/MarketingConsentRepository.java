package com.example.domain.member.repository;

import com.example.domain.member.entity.MarketingConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketingConsentRepository extends JpaRepository<MarketingConsent, Long> {

    @Query("SELECT mc1.member.id FROM MarketingConsent mc1 WHERE mc1.isAgreed = true AND mc1.date = (" +
            "  SELECT MAX(mc2.date) FROM MarketingConsent mc2 " +
            "  WHERE mc2.member = mc1.member)")
    List<Long> findLatestAgreedMembers();
}
