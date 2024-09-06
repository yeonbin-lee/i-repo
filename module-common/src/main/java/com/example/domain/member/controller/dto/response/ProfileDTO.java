package com.example.domain.member.controller.dto.response;

import lombok.Getter;

@Getter
public class ProfileDTO {

    private Long profileId;
    private String nickname;

    public ProfileDTO(Long profileId, String nickname) {
        this.profileId = profileId;
        this.nickname = nickname;
    }
}
