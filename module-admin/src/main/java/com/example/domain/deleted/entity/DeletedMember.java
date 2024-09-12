package com.example.domain.deleted.entity;

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
    private Long member_code; // member에서 사용하는 pk(id)값

    //    @Column(unique = true)
    private String email;

    //    @Column(unique = true)
    private String nickname;

    //    @Column(unique = true)
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "cancelled_at")
    private LocalDate cancelledAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy = "deletedMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeletedProfile> deletedProfiles = new ArrayList<DeletedProfile>();

    @Builder
    public DeletedMember(Long member_code, String email, String nickname, String phone, Gender gender, String password, LocalDate birthday,
                         LocalDate createdAt, LocalDate cancelledAt, Role role, Provider provider) {
        this.member_code = member_code;
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
}
