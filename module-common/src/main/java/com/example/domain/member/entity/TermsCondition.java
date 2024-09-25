package com.example.domain.member.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "terms_condition")
public class TermsCondition {
    @Id
    @Column(name = "terms_condition_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 약관 제목
    private String content; // 약관 내용
    private LocalDate createdAt; // 최초 생성 일자
}