package com.example.domain.deleted.service;

import com.example.domain.admin.controller.dto.request.RestoreRequest;
import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.DeletedProfile;
import com.example.domain.deleted.entity.Restore;
import com.example.domain.deleted.repository.DeletedMemberRepository;
import com.example.domain.deleted.repository.DeletedProfileRepository;
import com.example.domain.deleted.repository.RestoreRepository;
import com.example.domain.member.entity.Member;
import com.example.domain.member.service.MemberService;
import com.example.domain.member.service.ProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeletedMemberServiceImpl implements DeletedMemberService{

    private final MemberService memberService;
    private final DeletedMemberRepository deletedMemberRepository;
    private final ProfileService profileService;
    private final RestoreRepository restoreRepository; // 여기서만 사용해서 따로 service 클래스 생성X
    @Override
    @Transactional
    public void restoreMember(RestoreRequest request){
        DeletedMember deletedMember = findDeletedMemberByIdFetch(request.getDeletedMemberId());
        DeletedProfile deletedDefaultProfile = deletedMember.getDeletedDefaultProfile();
        List<DeletedProfile> deletedProfiles = deletedMember.getDeletedProfiles();

        Member member = memberService.convertDeletedMemberToMember(deletedMember);
        memberService.saveMember(member);

        profileService.convertDeletedDefaultProfileToDefaultProfile(member, deletedDefaultProfile);

        //profile도 id형식으로 변경
        for (DeletedProfile deletedProfile : deletedProfiles) {
            profileService.convertDeletedProfileToProfile(member, deletedProfile);
        }
        memberService.saveMember(member);

        Restore restore = Restore.builder()
                .id(member.getId())
                .email(member.getEmail())
                .date(LocalDate.now())
                .reason(request.getReason())
                .build();
        restoreRepository.save(restore);
        deleteDeletedMember(deletedMember);
    }


    @Transactional
    public DeletedMember findDeletedMemberByIdFetch(Long id) {
        return deletedMemberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("₩ 문제11")
        );
    }

    @Transactional
    public DeletedMember findDeletedMemberById(Long id) {
        return deletedMemberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("₩ 문제11")
        );
    }

    public List<DeletedMember> findAll(Specification<DeletedMember> spec) {
        return deletedMemberRepository.findAll(spec);
    }


    @Transactional
    public void deleteDeletedMember(DeletedMember deletedMember) {
        deletedMemberRepository.delete(deletedMember);
    }


}
