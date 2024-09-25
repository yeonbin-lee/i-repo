package com.example.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
public class MarketingConsent {

    @Id
    @Column(name = "marketing_consent_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "consent_state")
    private Boolean state; // 1 -> 수신 동의 상태, 0 -> 수신 동의 철회 상태

    @Column(name = "consent_date")
    private LocalDate consentDate;

    @Column(name = "withdraw_date")
    private LocalDate withdrawDate;

    @Builder
    public MarketingConsent(Long id, Member member, Boolean state, LocalDate consentDate, LocalDate withdrawDate) {
        this.id = id;
        this.member = member;
        this.state = state;
        this.consentDate = consentDate;
        this.withdrawDate = withdrawDate;
    }
}
