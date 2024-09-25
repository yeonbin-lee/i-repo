package com.example.domain.member.repository;

import com.example.domain.member.entity.TermsCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsConditionRepository extends JpaRepository<TermsCondition, Long> {
}
