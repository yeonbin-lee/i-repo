package com.example.domain.member.service.profile;

import com.example.domain.deleted.entity.DeletedProfile;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.Profile;
import com.example.domain.member.repository.ProfileRepository;

public interface ProfileService {
//    public Profile findMainProfile(Long memberId);
    public void convertDeletedDefaultProfileToDefaultProfile(Member member, DeletedProfile deletedProfile);
    public void convertDeletedProfileToProfile(Member member, DeletedProfile deletedProfile);
}
