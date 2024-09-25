package com.example.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public void setUpdatedAt(LocalDate updatedAt) {
//        this.createdAt = createdAt;
//    }


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Builder
    public TermsCondition(String title, String content, LocalDate createdAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
