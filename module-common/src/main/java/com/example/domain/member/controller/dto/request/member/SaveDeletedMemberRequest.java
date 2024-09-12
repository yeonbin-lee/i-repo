package com.example.domain.member.controller.dto.request.member;

import com.example.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class SaveDeletedMemberRequest {

    private Member member;
}
