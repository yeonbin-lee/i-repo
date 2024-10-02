package com.example.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "marketing_consent")
public class MarketingConsent {

    @Id
    @Column(name = "marketing_consent_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "is_agreed", nullable = false)
    private Boolean isAgreed; // 동의 여부를 나타내는 필드
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Builder
    public MarketingConsent(Member member, Boolean isAgreed, LocalDate date) {
        this.member = member;
        this.isAgreed = isAgreed;
        this.date = date;
    }
}
