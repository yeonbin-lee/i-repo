package com.example.domain.member.service;

import com.example.domain.member.controller.dto.request.profile.ProfileDeleteRequest;
import com.example.domain.member.controller.dto.request.profile.ProfileListRequest;
import com.example.domain.member.controller.dto.request.profile.ProfileRegisterRequest;
import com.example.domain.member.controller.dto.request.profile.ProfileUpdateRequest;
import com.example.domain.member.controller.dto.response.ProfileDTO;
import com.example.domain.member.entity.Profile;

import java.util.List;

public interface ProfileService {

    // 프로필 등록
    public void registerProfile(ProfileRegisterRequest request);

    // 프로필 업데이트
    public void updateProfile(ProfileUpdateRequest request);

    // 프로필 리스트
    public List<ProfileDTO> listProfile(ProfileListRequest request);

    // 프로필 삭제
    public void deleteProfile(ProfileDeleteRequest request);

    // 프로필 조회
    public Profile searchProfile(Long profileId);
}

