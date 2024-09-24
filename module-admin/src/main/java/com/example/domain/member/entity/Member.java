package com.example.domain.member.entity;

import com.example.domain.member.entity.enums.Gender;
import com.example.domain.member.entity.enums.Provider;
import com.example.domain.member.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member {

    @Id
    @Column(name = "member_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "default_profile_id")
    private Profile defaultProfile;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Profile> profiles = new ArrayList<Profile>();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<MemberTerm> memberTerms;


    @Builder
    public Member(Long id, String nickname, String email, String password, String phone, Gender gender, LocalDate birthday, LocalDate createdAt, Role role, Provider provider) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.role = role;
        this.provider = provider;
    }

    public void updatePhone(String phone) {this.phone = phone;}
    public void updatePassword(String password){
        this.password = password;
    }
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    // 기본 프로필 설정 메서드
    public void setDefaultProfile(Profile defaultProfile) {
        this.defaultProfile = defaultProfile;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

}
