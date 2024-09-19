package com.example.domain.member.mapper;

import com.example.domain.admin.controller.dto.response.FilterResponse;
import com.example.domain.member.entity.Member;

public class MemberMapper {

    public static FilterResponse toFilterResponse(Member member) {
        FilterResponse response = new FilterResponse();
        response.setMember_id(member.getId().toString());
        response.setEmail(member.getEmail());
        response.setNickname(member.getNickname());
        response.setCreatedAt(member.getCreatedAt().toString());
        response.setRole(member.getRole());
        response.setBirthday(member.getBirthday());
        response.setGender(member.getGender());

        if (member.getDefaultProfile()!= null) {
            response.setPregnancy(member.getDefaultProfile().getPregnancy());
            response.setSmoking(member.getDefaultProfile().getSmoking());
            response.setHypertension(member.getDefaultProfile().getHypertension());
            response.setDiabetes(member.getDefaultProfile().getDiabetes());
        }

        return response;
    }
}
