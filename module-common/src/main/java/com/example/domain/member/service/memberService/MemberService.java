package com.example.domain.member.service.memberService;

import com.example.domain.member.controller.dto.request.member.NicknameChangeRequest;
import com.example.domain.member.controller.dto.request.member.PwChangeRequest;
import com.example.domain.member.controller.dto.response.MemberResponse;
import com.example.domain.member.entity.Member;

import java.util.Optional;

public interface MemberService {

    public MemberResponse findById(String accessToken);
//    public void update(Long id, MemberUpdateDTO requestDto);
    public Boolean existByEmail(String email);
    public Boolean existByPhone(String phone);
    public Boolean existByNickname(String nickname);
    public void saveMember(Member member);
    public Member findByPhone(String phone);
    public Member findByEmail(String email);
    public void changePassword(String accessToken, PwChangeRequest request);
    public void changeNickname(String accessToken, NicknameChangeRequest request);
    public Optional<Member> findOpMemberByEmail(String email);
    public Member findByEmailAndPhone(String email, String phone);
    public Member findMemberById(Long memberId);
    public Long findMemberIdByAccessToken(String accessToken);
    public void deleteMember(Member member);
    public void delete(String accessToken);
    public void logout(String accessToken, String email);
}
