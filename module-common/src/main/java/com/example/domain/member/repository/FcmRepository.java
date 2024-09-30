package com.example.domain.member.repository;

import com.example.domain.member.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmRepository extends JpaRepository<FCMToken, Long> {

    FCMToken findByMemberId(Long memberId);
}
