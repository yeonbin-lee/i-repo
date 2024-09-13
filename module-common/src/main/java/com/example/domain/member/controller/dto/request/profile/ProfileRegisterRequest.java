package com.example.domain.member.controller.dto.request.profile;

import com.example.domain.member.entity.enums.Choice;
import com.example.domain.member.entity.enums.Gender;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProfileRegisterRequest {

    private String email;

    private String nickname;

    private Gender gender;

    private LocalDate birthday;

    private Choice pregnancy;

    private Choice smoking;

    private Choice hypertension;

    private Choice diabetes;

//    public Profile toEntity() {
//        return Profile.builder()
//                .nickname(nickname)
//                .owner(Boolean.FALSE)
//                .gender(gender)
//                .birthday(birthday)
//                .pregnancy(pregnancy)
//                .smoking(smoking)
//                .hypertension(hypertension)
//                .diabetes(diabetes)
//                .build();
//    }

}

