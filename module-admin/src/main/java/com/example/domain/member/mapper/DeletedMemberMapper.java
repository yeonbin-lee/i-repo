package com.example.domain.member.mapper;

import com.example.domain.admin.controller.dto.response.DeletedFilterResponse;
import com.example.domain.deleted.entity.DeletedMember;

public class DeletedMemberMapper {
    public static DeletedFilterResponse toFilterResponse(DeletedMember deletedMember) {
        DeletedFilterResponse response = new DeletedFilterResponse();
        response.setMember_id(deletedMember.getId().toString());
        response.setEmail(deletedMember.getEmail());
        response.setNickname(deletedMember.getNickname());
        response.setCreatedAt(deletedMember.getCreatedAt().toString());
        response.setCancelledAt(deletedMember.getCancelledAt().toString());
        response.setRole(deletedMember.getRole());
        response.setBirthday(deletedMember.getBirthday());
        response.setGender(deletedMember.getGender());
        response.setReason(deletedMember.getResignationReason());
        if (deletedMember.getDeletedDefaultProfile()!= null) {
            response.setPregnancy(deletedMember.getDeletedDefaultProfile().getPregnancy());
            response.setSmoking(deletedMember.getDeletedDefaultProfile().getSmoking());
            response.setHypertension(deletedMember.getDeletedDefaultProfile().getHypertension());
            response.setDiabetes(deletedMember.getDeletedDefaultProfile().getDiabetes());
        }

        return response;
    }
}