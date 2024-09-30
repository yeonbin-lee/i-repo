package com.example.domain.member.service.profile;

import com.example.domain.deleted.entity.DeletedProfile;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.Profile;
import com.example.domain.member.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    private Profile saveProfile(Profile profile){
        return profileRepository.save(profile);
    }

    /**
     * 기본 프로필 변환
     */
    @Override
    public void convertDeletedDefaultProfileToDefaultProfile(Member member, DeletedProfile deletedProfile) {
        System.out.println("들어오냐?");
        Profile profile = Profile.builder()
                .id(deletedProfile.getId())
                .owner(deletedProfile.getOwner())
                .nickname(deletedProfile.getNickname())
                .gender(deletedProfile.getGender())
                .birthday(deletedProfile.getBirthday())
                .pregnancy(deletedProfile.getPregnancy())
                .smoking(deletedProfile.getSmoking())
                .hypertension(deletedProfile.getHypertension())
                .diabetes(deletedProfile.getDiabetes())
                .build();
        System.out.println("시바시바시밧");
        member.setDefaultProfile(saveProfile(profile));
    }

    @Override
    public void convertDeletedProfileToProfile(Member member, DeletedProfile deletedProfile) {
        Profile profile = Profile.builder()
                .id(deletedProfile.getId())
                .member(member)
                .nickname(deletedProfile.getNickname())
                .gender(deletedProfile.getGender())
                .birthday(deletedProfile.getBirthday())
                .owner(deletedProfile.getOwner())
                .pregnancy(deletedProfile.getPregnancy())
                .smoking(deletedProfile.getSmoking())
                .hypertension(deletedProfile.getHypertension())
                .diabetes(deletedProfile.getDiabetes())
                .build();
        saveProfile(profile);
    }

}
