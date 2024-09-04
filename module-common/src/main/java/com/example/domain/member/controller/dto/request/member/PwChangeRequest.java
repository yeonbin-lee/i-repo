package com.example.domain.member.controller.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PwChangeRequest {

    @NotBlank
    private String originPassword;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", message = "비밀번호는 8~15자 영문, 숫자, 특수문자를 사용하세요.")
    private String newPassword;


}
