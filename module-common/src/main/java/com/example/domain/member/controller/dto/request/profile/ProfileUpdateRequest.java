package com.example.domain.member.controller.dto.request.profile;

import com.example.domain.member.entity.enums.Choice;
import lombok.Getter;

@Getter
public class ProfileUpdateRequest {

    private Long profile_id;

    private Choice pregnancy;

    private Choice smoking;

    private Choice hypertension;

    private Choice diabetes;
}
