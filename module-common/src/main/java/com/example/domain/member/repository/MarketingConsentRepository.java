package com.example.domain.member.repository;

import com.example.domain.member.entity.MarketingConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketingConsentRepository extends JpaRepository<MarketingConsent, Long> {
}
