package com.example.domain.auth.controller.dto.response;

import com.example.domain.member.entity.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FindEmailResponse {
    private Provider provider;
    private String email;

}
