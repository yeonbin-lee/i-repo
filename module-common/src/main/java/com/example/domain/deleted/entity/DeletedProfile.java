package com.example.domain.deleted.entity;

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
@Entity
@Table(name = "deleted_profile")
public class DeletedProfile {

    @Id
    @Column(name = "deleted_profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "deleted_member_id")
    private DeletedMember deletedMember;

    @Column
    private Long profile_id; // 기존 profile_id

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
    public DeletedProfile(DeletedMember deletedMember, Long profile_id,String nickname, Gender gender, LocalDate birthday, Boolean owner
            , Choice pregnancy, Choice smoking, Choice hypertension, Choice diabetes) {
        this.deletedMember = deletedMember;
        this.profile_id = profile_id;
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