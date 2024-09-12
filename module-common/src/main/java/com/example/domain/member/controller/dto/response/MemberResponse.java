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

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.phone = member.getPhone();
        this.gender = member.getGender().name();
        this.birthday = member.getBirthday();
        // Enum -> String
        this.role = member.getRole().name();
    }
}
