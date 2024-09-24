package com.example.domain.deleted.entity;

import com.example.domain.deleted.entity.enums.ResignationReason;
import com.example.domain.member.entity.enums.Gender;
import com.example.domain.member.entity.enums.Provider;
import com.example.domain.member.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "deleted_member")
public class DeletedMember {
    @Id
    @Column(name = "deleted_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String nickname;

    @Column
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private LocalDate birthday;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created_at")
    private LocalDate createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "cancelled_at")
    private LocalDate cancelledAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_default_profile_id")
    private DeletedProfile deletedDefaultProfile;

    @OneToMany(mappedBy = "deletedMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeletedProfile> deletedProfiles = new ArrayList<DeletedProfile>();

    @Enumerated(EnumType.STRING) // Enum을 사용할 경우
    private ResignationReason resignationReason;

    @Builder
    public DeletedMember(Long member_id, String email, String nickname, String phone, Gender gender, String password, LocalDate birthday,
                         LocalDate createdAt, LocalDate cancelledAt, Role role, Provider provider) {
        this.id = member_id;
        this.email= email;
        this.nickname = nickname;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.cancelledAt = cancelledAt;
        this.role = role;
        this.provider = provider;
    }

    // 기본 프로필 설정 메서드
    public void setDeletedDefaultProfile(DeletedProfile defaultProfile) {
        this.deletedDefaultProfile = deletedDefaultProfile;
    }
}
