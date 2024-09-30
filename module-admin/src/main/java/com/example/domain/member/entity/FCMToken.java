package com.example.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "FCMToken",
        uniqueConstraints =  {
                @UniqueConstraint(columnNames = {"member_id", "fcm_id"})
        }
)
public class FCMToken {

    @Id
    @Column(name = "fcm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String token;

    private LocalDate createdAt;

    public void setToken(String token) {
        this.token = token;
    }

    @Builder
    public FCMToken(Member member, String token, LocalDate createdAt) {
        this.member = member;
        this.token = token;
        this.createdAt = createdAt;
    }
}

