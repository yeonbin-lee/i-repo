package com.example.domain.member.service.member;

import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.member.entity.Member;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface MemberService {

    public Member convertDeletedMemberToMember(DeletedMember deletedMember);
    public void saveMember(Member member);

    public List<Member> findAll(Specification<Member> spec);
    public List<Member> findAllMember();
}
