package com.example.domain.member.controller.dto.request.member;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckEmailDuplicateRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
}
