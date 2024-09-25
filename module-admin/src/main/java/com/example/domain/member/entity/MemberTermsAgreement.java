package com.example.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Table(name = "member_terms_agreement",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"member_id", "terms_condition_id"})
        }
)
public class MemberTermsAgreement {
    @Id
    @Column(name = "member_terms_agreement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "terms_condition_id")
    private TermsCondition termsCondition;

    @Column(name = "is_agreed", nullable = false)
    private Boolean isAgreed; // 동의 여부를 나타내는 필드

    @Column(name = "agreed_at")
    private LocalDate agreedAt; // 동의 일자


    @Builder
    public MemberTermsAgreement(Member member, TermsCondition termsCondition, Boolean isAgreed, LocalDate agreedAt) {
        this.member = member;
        this.termsCondition = termsCondition;
        this.isAgreed = isAgreed;
        this.agreedAt = agreedAt;
    }
}