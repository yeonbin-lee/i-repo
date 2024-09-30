package com.example.domain.member.repository;

import com.example.domain.member.entity.MarketingConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketingConsentRepository extends JpaRepository<MarketingConsent, Long> {

//    @Query("SELECT mc FROM MarketingConsent mc WHERE mc.isAgreed = true AND mc.date = " +
//            "(SELECT MAX(mc2.date) FROM MarketingConsent mc2 WHERE mc2.member.id = mc.member.id)")
//    List<MarketingConsent> findMostRecentAgreedConsents();

//    @Query("SELECT mc FROM MarketingConsent mc WHERE mc.isAgreed = true " +
//            "AND mc.date = (SELECT MAX(innerMc.date) FROM MarketingConsent innerMc WHERE innerMc.member.id = mc.member.id) ")
//    List<MarketingConsent> findMostRecentAgreedConsents1();

    @Query("SELECT f.token FROM FCMToken f JOIN MarketingConsent mc ON f.member.id = mc.member.id "+
            "WHERE mc.isAgreed = true AND mc.date = (SELECT MAX(innerMc.date) FROM MarketingConsent innerMc WHERE innerMc.member.id = mc.member.id)" )
    List<String> findMostRecentAgreedConsents1();


}
