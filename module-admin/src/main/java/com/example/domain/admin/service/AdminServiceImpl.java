package com.example.domain.admin.service;

import com.example.domain.admin.controller.dto.RestoreRequest;
import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.DeletedProfile;
import com.example.domain.deleted.repository.DeletedMemberRepository;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.Profile;
import com.example.domain.member.repository.MemberRepository;
import com.example.domain.member.repository.ProfileRepository;
import com.example.domain.member.service.MemberService;
import com.example.domain.member.service.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final DeletedMemberRepository deletedMemberRepository;
    private final MemberService memberService;
    private final ProfileService profileService;

    @Transactional
    @Override
    public void restoreMember(RestoreRequest request){

//        DeletedMember deletedMember = deletedMemberRepository.findById(request.getId()).get();

        DeletedMember deletedMember = findByIdWithProfiles(request.getDeletedMemberId());

        List<DeletedProfile> deletedProfiles = deletedMember.getDeletedProfiles();
        Member member = convertDeletedMemberToMember(deletedMember);
        memberService.saveMember(member);
        for (DeletedProfile deletedProfile : deletedMember.getDeletedProfiles()) {
            profileService.saveProfile(convertDeletedProfileToProfile(member, deletedProfile));
        }
        deletedMemberRepository.delete(deletedMember);
    }


    private DeletedMember findByIdWithProfiles(Long id){
        return deletedMemberRepository.findByIdWithProfiles(id).orElseThrow(
                () -> new NotFoundException("존재하지않는 Deleted Member")
        );
    }

    private Member convertDeletedMemberToMember(DeletedMember deletedMember) {
        return Member.builder()
                .id(deletedMember.getMember_code())
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

    private Profile convertDeletedProfileToProfile(Member member, DeletedProfile deletedProfile) {
        return Profile.builder()
                .id(deletedProfile.getProfile_id())
                .member(member)
                .nickname(member.getNickname())
                .gender(deletedProfile.getGender())
                .birthday(deletedProfile.getBirthday())
                .owner(deletedProfile.getOwner())
                .pregnancy(deletedProfile.getPregnancy())
                .smoking(deletedProfile.getSmoking())
                .hypertension(deletedProfile.getHypertension())
                .diabetes(deletedProfile.getDiabetes())
                .build();
    }

}
