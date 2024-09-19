package com.example.domain.member.entity;

import com.example.domain.member.entity.enums.Choice;
import com.example.domain.member.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Profile {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column
    private Boolean owner; // 본인인지, 아닌지 True, False

    @Enumerated(EnumType.STRING)
    private Choice pregnancy;

    @Enumerated(EnumType.STRING)
    private Choice smoking;

    @Enumerated(EnumType.STRING)
    private Choice hypertension;

    @Enumerated(EnumType.STRING)
    private Choice diabetes;

    @Builder
    public Profile(Long id, Member member, String nickname, Gender gender, LocalDate birthday, Boolean owner
            , Choice pregnancy, Choice smoking, Choice hypertension, Choice diabetes) {
        this.id = id;
        this.member = member;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.owner = owner;
        this.pregnancy = pregnancy;
        this.smoking = smoking;
        this.hypertension = hypertension;
        this.diabetes = diabetes;
    }
}
