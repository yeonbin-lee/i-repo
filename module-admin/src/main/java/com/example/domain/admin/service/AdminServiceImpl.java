package com.example.domain.admin.service;

import com.example.domain.admin.controller.dto.RestoreRequest;
import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.DeletedProfile;
import com.example.domain.deleted.repository.DeletedMemberRepository;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.Profile;
import com.example.domain.member.repository.MemberRepository;
import com.example.domain.member.repository.MemberSpecification;
import com.example.domain.member.repository.ProfileRepository;
import com.example.domain.member.service.MemberService;
import com.example.domain.member.service.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final DeletedMemberRepository deletedMemberRepository;
    private final MemberService memberService;
    private final ProfileService profileService;
    private final MemberRepository memberRepository;

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

    public List<Member> searchMembers(LocalDate startDate, LocalDate endDate, boolean isBirthDayFilter, Long memberId, String email, String nickname, String gender) {
        Specification<Member> spec = Specification.where(null);

        Profile mainProfile = profileService.findMainProfile(memberId);

        // 날짜 필터 (생년월일 또는 회원 가입일)
        if (startDate != null && endDate != null) {
            if (isBirthDayFilter) {
                spec = spec.and(MemberSpecification.betweenBirthDay(startDate, endDate)); // 생년월일 기준 필터
            } else {
                spec = spec.and(MemberSpecification.betweenCreatedAt(startDate, endDate)); // 회원 가입일 기준 필터
            }
        }

        // 검색 조건 (회원번호, 이메일, 닉네임, 성별 중 하나)
        if (memberId != null) {
            spec = spec.and(MemberSpecification.hasMemberId(memberId));
        } else if (email != null && !email.isEmpty()) {
            spec = spec.and(MemberSpecification.hasEmail(email));
        } else if (nickname != null && !nickname.isEmpty()) {
            spec = spec.and(MemberSpecification.hasNickname(nickname));
        } else if (gender != null && !gender.isEmpty()) {
            spec = spec.and(MemberSpecification.hasGender(gender));
        }
        return memberRepository.findAll(spec);

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
