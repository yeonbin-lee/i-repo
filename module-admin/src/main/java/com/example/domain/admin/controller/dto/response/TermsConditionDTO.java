package com.example.domain.admin.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TermsConditionDTO {
    private Long id;
    private String title;


    // 생성자
    public TermsConditionDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
