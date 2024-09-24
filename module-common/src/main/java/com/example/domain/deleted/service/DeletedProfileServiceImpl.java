package com.example.domain.deleted.service;

import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.DeletedProfile;
import com.example.domain.deleted.repository.DeletedProfileRepository;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.Profile;
import com.example.domain.member.entity.enums.Choice;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletedProfileServiceImpl implements DeletedProfileService{

    private final DeletedProfileRepository deletedProfileRepository;

    /**
     * 기본 프로필 변환
     */
    @Override
    public void convertDefaultProfileToDeletedDefaultProfile(DeletedMember deletedMember, Profile profile) {
        DeletedProfile deletedProfile = DeletedProfile.builder()
                .profile_id(profile.getId())
                .owner(true)
                .nickname(profile.getNickname())
                .gender(profile.getGender())
                .birthday(profile.getBirthday())
                .pregnancy(profile.getPregnancy())
                .smoking(profile.getSmoking())
                .hypertension(profile.getHypertension())
                .diabetes(profile.getDiabetes())
                .build();
        saveDeletedProfile(deletedProfile);
        deletedMember.setDefaultProfile(deletedProfile);
    }

    /**
     * 추가 프로필 변환
     */
    @Override
    public void convertProfileToDeletedProfile(DeletedMember deletedMember, Profile profile) {
        DeletedProfile deletedProfile = DeletedProfile.builder()
                .deletedMember(deletedMember)
                .profile_id(profile.getId())
                .owner(false)
                .nickname(profile.getNickname())
                .gender(profile.getGender())
                .birthday(profile.getBirthday())
                .pregnancy(profile.getPregnancy())
                .smoking(profile.getSmoking())
                .hypertension(profile.getHypertension())
                .diabetes(profile.getDiabetes())
                .build();
        saveDeletedProfile(deletedProfile);
        deletedMember.getDeletedProfiles().add(deletedProfile);
//        deletedMember.setDefaultProfile(deletedProfile);
    }


//    /**
//     * 추가 프로필 등록
//     */
//    @Override
//    public void convertProfileToDeletedProfile(DeletedMember deletedMember, Profile profile) {
//        DeletedProfile deletedProfile = DeletedProfile.builder()
//                .deletedMember(deletedMember)
//                .deleted_profile_id(profile.getId())
//                .nickname(profile.getNickname())
//                .gender(profile.getGender())
//                .birthday(profile.getBirthday())
//                .pregnancy(profile.getPregnancy())
//                .smoking(profile.getSmoking())
//                .hypertension(profile.getHypertension())
//                .diabetes(profile.getDiabetes())
//                .build();
//        saveDeletedProfile(deletedProfile);
//    }



    @Transactional
    public DeletedProfile saveDeletedProfile(DeletedProfile deletedProfile) {
        return deletedProfileRepository.save(deletedProfile);
    }
}
