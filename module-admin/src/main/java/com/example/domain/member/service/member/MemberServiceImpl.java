package com.example.domain.member.service.member;

import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.member.entity.Member;
import com.example.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member convertDeletedMemberToMember(DeletedMember deletedMember){
        return Member.builder()
                .id(deletedMember.getId())
                .email(deletedMember.getEmail())
                .nickname(deletedMember.getNickname())
                .phone(deletedMember.getPhone())
                .gender(deletedMember.getGender())
                .password(deletedMember.getPassword())
                .birthday(deletedMember.getBirthday())
                .createdAt(deletedMember.getCreatedAt())
                .role(deletedMember.getRole())
                .provider(deletedMember.getProvider())
                .build();
    }

    public List<Member> findAllMember(){
        return memberRepository.findAll();
    }


    public List<Member> findAll(Specification<Member> spec) {
        return memberRepository.findAll(spec);
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}
