package com.example.domain.deleted.service;

import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.enums.ResignationReason;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.Profile;
import org.springframework.web.bind.annotation.RequestParam;

public interface DeletedMemberService {

    public void saveDeletedMember(DeletedMember deletedMember);
    public DeletedMember convertMemberToDeletedMember(Member member, ResignationReason reason);
}
