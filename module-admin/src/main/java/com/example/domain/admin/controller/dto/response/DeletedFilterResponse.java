package com.example.domain.admin.controller.dto.response;

import com.example.domain.deleted.entity.enums.ResignationReason;
import com.example.domain.member.entity.enums.Choice;
import com.example.domain.member.entity.enums.Gender;
import com.example.domain.member.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DeletedFilterResponse {
    private String member_id;
    private String email;
    private String nickname;
    private String createdAt;
    private String cancelledAt;
    private Role role;
    private LocalDate birthday;
    private Gender gender;
    private Choice pregnancy;
    private Choice smoking;
    private Choice hypertension;
    private Choice diabetes;
    private ResignationReason reason;
}
