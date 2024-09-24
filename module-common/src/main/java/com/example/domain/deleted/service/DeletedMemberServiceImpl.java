package com.example.domain.deleted.service;

import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.enums.ResignationReason;
import com.example.domain.deleted.repository.DeletedMemberRepository;
import com.example.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeletedMemberServiceImpl implements DeletedMemberService {

    private final DeletedMemberRepository deletedMemberRepository;
    private final DeletedProfileService deletedProfileService;


    @Override
    @Transactional
    public DeletedMember convertMemberToDeletedMember(Member member, ResignationReason reason){

        return DeletedMember.builder()
                .member_id(member.getId())
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
                .reason(reason)
                .build();
    }


    @Override
    public void saveDeletedMember(DeletedMember deletedMember) {
        deletedMemberRepository.save(deletedMember);
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
