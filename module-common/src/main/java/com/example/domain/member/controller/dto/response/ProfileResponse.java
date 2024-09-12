package com.example.domain.member.controller.dto.response;

import com.example.domain.member.entity.Profile;
import com.example.domain.member.entity.enums.Choice;
import com.example.domain.member.entity.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProfileResponse {

    private Long id;
    private String nickname;

    private LocalDate birthday;

    private Gender gender;

    private Choice pregnancy;

    private Choice smoking;

    private Choice hypertension;

    private Choice diabetes;


    public ProfileResponse(Profile profile) {
        this.id = profile.getId();
        this.nickname = profile.getNickname();
        this.birthday = profile.getBirthday();
        this.gender = profile.getGender();
        this.pregnancy = profile.getPregnancy();
        this.smoking = profile.getSmoking();
        this.hypertension = profile.getHypertension();
        this.diabetes = profile.getDiabetes();
    }
}
