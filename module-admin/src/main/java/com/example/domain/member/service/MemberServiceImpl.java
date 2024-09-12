package com.example.domain.member.service;

import com.example.domain.member.entity.Member;
import com.example.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}
