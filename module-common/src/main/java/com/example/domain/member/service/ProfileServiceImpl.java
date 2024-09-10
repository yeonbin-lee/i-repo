package com.example.domain.member.service;

import com.example.domain.member.controller.dto.request.profile.ProfileDeleteRequest;
import com.example.domain.member.controller.dto.request.profile.ProfileListRequest;
import com.example.domain.member.controller.dto.request.profile.ProfileRegisterRequest;
import com.example.domain.member.controller.dto.request.profile.ProfileUpdateRequest;
import com.example.domain.member.controller.dto.response.ProfileDTO;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.Profile;
import com.example.domain.member.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final MemberService memberService;

    /**
     * 프로필 등록
     * */
    @Override
    public void registerProfile(ProfileRegisterRequest request) {

        Member member = memberService.findByEmail(request.getEmail());
        List<Profile> profiles = member.getProfiles();

        // 프로필 개수 확인 (최대 10개 가능)
        if(profileRepository.countAllProfiles() > 10) {
            throw new DataIntegrityViolationException("최대 프로필 개수는 10개 입니다.");
        }

        // 기본 프로필 존재 여부 확인 (1개만 가능)
        if(request.getOwner() == true){
            if(profileRepository.countProfilesWithOwnerTrue() > 0){
                throw new DataIntegrityViolationException("이미 기본 프로필이 존재합니다.");
            }
        }

        // 중복 닉네임 체크
        if(profiles.stream()
                .anyMatch(profile -> profile.getNickname().equals(request.getNickname()))) {
            throw new DataIntegrityViolationException("중복되는 닉네임입니다.");
        }

        Profile profile = Profile.builder()
                .member(member)
                .nickname(request.getNickname())
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .owner(request.getOwner())
                .pregnancy(request.getPregnancy())
                .smoking(request.getSmoking())
                .hypertension(request.getHypertension())
                .diabetes(request.getDiabetes())
                .build();
        saveProfile(profile);
    }

    /**
     * 프로필 업데이트 - 당뇨병, 고혈압, 임신, 담배 유무만 변경 가능
     */
    @Override
    public void updateProfile(ProfileUpdateRequest request) {
        Profile profile = searchProfile(request.getProfileId());

        profile.setDiabetes(request.getDiabetes());
        profile.setHypertension(request.getHypertension());
        profile.setPregnancy(request.getPregnancy());
        profile.setSmoking(request.getSmoking());

        saveProfile(profile);
    }

    /**
     * 프로필 리스트
     * profileId, nickname을 포함하는 DTO List로 반환
     */
    @Override
    public List<ProfileDTO> listProfile(ProfileListRequest request) {
        List<Profile> profiles = memberService.findByEmail(request.getEmail()).getProfiles();
        System.out.println(profiles);
        List<ProfileDTO> profileVOList = new ArrayList<>();

        for (Profile profile : profiles) {
            ProfileDTO profileDTO = new ProfileDTO(profile.getId(), profile.getNickname());
            profileVOList.add(profileDTO);
        }
        return profileVOList;
    }

    /**
     * 프로필 삭제
     * 기본 프로필 삭제 불가, 추가 프로필만 삭제 가능
     */
    @Override
    public void deleteProfile(ProfileDeleteRequest request){
        Long profileId = request.getProfileId();
        Profile profile = searchProfile(request.getProfileId());
        if(profile.getOwner() == true) {
            throw new IllegalArgumentException("기본 프로필은 삭제할 수 없습니다.");
        }
        deleteProfileById(profileId);
    }

    /**
     * 프로필 조회
     */
    @Override
    public Profile searchProfile(Long profileId){
        return profileRepository.findById(profileId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 프로필")
        );
    }


    private void deleteProfileById(Long profileId){
        profileRepository.deleteById(profileId);
    }

    private void saveProfile(Profile profile){
        profileRepository.save(profile);
    }

//    private Profile findProfileById(Long profileId){
//        return profileRepository.findById(profileId).orElseThrow(
//                () -> new IllegalArgumentException("존재하지 않는 프로필")
//        );
//    }
}