package com.example.domain.member.repository;

import com.example.domain.member.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

    @Query("SELECT f.token FROM FCMToken f WHERE f.member.id IN :agreedMembers")
    List<String> findTokensByMemberIds(List<Long> agreedMembers);

    @Query("SELECT f.token FROM FCMToken f WHERE f.member.id = :memberId")
    String findTokenByMemberId(Long memberId);
}

