package com.example.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "system_consent")
public class SystemConsent {

    @Id
    @Column(name = "system_consent_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "is_agreed", nullable = false)
    private Boolean isAgreed; // 동의 여부를 나타내는 필드
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;
    @Builder
    public SystemConsent(Member member, Boolean isAgreed, LocalDate date) {
        this.member = member;
        this.isAgreed = isAgreed;
        this.date = date;
    }
}
