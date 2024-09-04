package com.example.domain.member.controller.dto.response;

import com.example.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberResponse {
    private Long id;
    private String role;
    private String email;
    private String nickname;
    private String phone;
    private String gender;
    private LocalDate birthday;

    public MemberResponse(Member entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.nickname = entity.getNickname();
        this.phone = entity.getPhone();
        this.gender = entity.getGender().name();
        this.birthday = entity.getBirthday();
        // Enum -> String
        this.role = entity.getRole().name();
    }
}
