package com.example.domain.member.service;

import com.example.domain.member.controller.dto.request.member.FindEmailByPhoneRequest;
import com.example.domain.member.controller.dto.request.member.NicknameChangeRequest;
import com.example.domain.member.controller.dto.request.member.PwChangeRequest;
import com.example.domain.member.controller.dto.request.member.PwFindRequest;
import com.example.domain.member.controller.dto.response.MemberResponse;

public interface MemberService {

    public MemberResponse findById(String accessToken);
    //    public void update(Long id, MemberUpdateDTO requestDto);
//    public void delete(String accessToken);
//    public boolean checkDuplicateEmail(String email);
//    public String findEmailByPhone(FindEmailByPhoneRequest findEmailByPhoneRequest);
//    public void changePasswordV1(PwFindRequest pwFindRequest);
//    public void changePasswordV2(String accessToken, PwChangeRequest pwChangeRequest);
//    public void changeNickname(String accessToken, NicknameChangeRequest nicknameChangeRequest);

}
