package com.example.domain.deleted.service;

import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.DeletedProfile;
import com.example.domain.deleted.entity.enums.ResignationReason;
import com.example.domain.deleted.repository.DeletedMemberRepository;
import com.example.domain.deleted.repository.DeletedProfileRepository;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.Profile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletedMemberServiceImpl implements DeletedMemberService {

    private final DeletedMemberRepository deletedMemberRepository;
    private final DeletedProfileRepository deletedProfileRepository;

    @Override
    public void saveDeletedMember(Member member, ResignationReason reason){

        DeletedMember deletedMember = convertMemberToDeletedMember(member);
        deletedMember.setResignationReason(reason);
        deletedMemberRepository.save(deletedMember);

        for (Profile profile : member.getProfiles()) {
            deletedProfileRepository.save(convertProfileToDeletedProfile(deletedMember, profile));
        }
    }

    private DeletedMember convertMemberToDeletedMember(Member member){

        return DeletedMember.builder()
                .member_code(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .gender(member.getGender())
                .password(member.getPassword())
                .birthday(member.getBirthday())
                .createdAt(member.getCreatedAt())
                .cancelledAt(LocalDate.now())
                .role(member.getRole())
                .provider(member.getProvider())
                .build();
    }

    private DeletedProfile convertProfileToDeletedProfile(DeletedMember deletedMember, Profile profile) {
        // 프로필 변환 로직
        return DeletedProfile.builder()
                .deletedMember(deletedMember)
                .profile_id(profile.getId())
                .nickname(profile.getNickname())
                .gender(profile.getGender())
                .birthday(profile.getBirthday())
                .owner(profile.getOwner())
                .pregnancy(profile.getPregnancy())
                .smoking(profile.getSmoking())
                .hypertension(profile.getHypertension())
                .diabetes(profile.getDiabetes())
                .build();
    }

    // 매일 자정에 삭제 작업을 실행 (cron 표현식으로 스케줄 설정)
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    @Transactional
    public void cleanupDeletedMembers() {
        // 예: 3년 전에 취소된 회원을 삭제하는 로직
        LocalDate threeYearsAgo = LocalDate.now().minusYears(3);

        List<DeletedMember> membersToDelete = deletedMemberRepository
                .findByCancelledAtBefore(threeYearsAgo);

        for (DeletedMember member : membersToDelete) {
            deletedMemberRepository.delete(member);
        }

        System.out.println("Deleted members older than 3 years.");
    }

}
