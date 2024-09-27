package com.example.domain.member.repository;

import com.example.domain.member.entity.SystemConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConsentRepository extends JpaRepository<SystemConsent, Long> {
}
