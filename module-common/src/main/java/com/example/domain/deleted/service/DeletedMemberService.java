package com.example.domain.deleted.service;

import com.example.domain.deleted.entity.enums.ResignationReason;
import com.example.domain.member.entity.Member;
import org.springframework.web.bind.annotation.RequestParam;

public interface DeletedMemberService {

    public void saveDeletedMember(Member member, ResignationReason reason);
}
