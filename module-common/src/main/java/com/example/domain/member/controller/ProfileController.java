package com.example.domain.member.controller;

import com.example.domain.member.controller.dto.request.profile.ProfileDeleteRequest;
import com.example.domain.member.controller.dto.request.profile.ProfileListRequest;
import com.example.domain.member.controller.dto.request.profile.ProfileRegisterRequest;
import com.example.domain.member.controller.dto.request.profile.ProfileUpdateRequest;
import com.example.domain.member.entity.Profile;
import com.example.domain.member.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;


    /**
     * 프로필 등록 API
     * - 프로필 내 중복 닉네임 불가능
     * - 기본 프로필은 최대 1개 등록 가능
     * - (기본 + 추가) 프로필 최대 10개 가능
     * */
    @PostMapping("/register")
    public ResponseEntity<?> registerProfile(@RequestBody ProfileRegisterRequest request) {
        profileService.registerProfile(request);
        return ResponseEntity.status(HttpStatus.OK).body("Profile registered successfully!");
    }


    /**
     * 프로필 업데이트 API - 당뇨병, 고혈압, 임신, 담배 유무 변경 가능
     * */
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest request) {
        profileService.updateProfile(request);
        return ResponseEntity.status(HttpStatus.OK).body("profile updated successfully!");
    }

    /**
     * 프로필 리스트 API
     * */
    @GetMapping("/list")
    public ResponseEntity<?> listProfile(@RequestBody ProfileListRequest request) {
        List<Profile> profiles = profileService.listProfile(request);
        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }

    /**
     * 프로필 삭제 API
     * */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProfile(@RequestBody ProfileDeleteRequest request) {
        profileService.deleteProfile(request);
        return ResponseEntity.status(HttpStatus.OK).body("Profile deleted successfully!");
    }

    /**
     * 프로필 조회 API
     * */
    @GetMapping("/search/{profile_id}")
    public ResponseEntity<?> searchProfile(@PathVariable Long profileId) {
        Profile profile = profileService.searchProfile(profileId);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

}
