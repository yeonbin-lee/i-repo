package com.example.domain.member.service;

import com.example.domain.admin.controller.dto.request.CreateTermsConditionRequest;
import com.example.domain.admin.controller.dto.request.DeleteTermsCondition;
import com.example.domain.admin.controller.dto.request.UpdateTermsConditionRequest;
import com.example.domain.admin.controller.dto.response.TermsConditionDTO;
import com.example.domain.member.entity.TermsCondition;
import com.example.domain.member.repository.TermsConditionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermsConditionServiceImpl implements TermsConditionService{

    private final TermsConditionRepository termsConditionRepository;
    private final MemberTermsAgreementService memberTermsAgreementService;

    @Override
    @Transactional
    public void createMandatoryTermsCondition(CreateTermsConditionRequest request) {

        TermsCondition termsCondition = TermsCondition.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDate.now())
                .build();

        termsConditionRepository.save(termsCondition);

        // 필수 동의 체크를 위한 MemberTermsCondition -> 자동 생성/ User의 동의를 구하지 않음
        // 모든 멤버에 대해 동의 사항 생성
        memberTermsAgreementService.updateAllMembersToAgreeToTerms(termsCondition);
    }

    public void createOptionalTermsCondition(CreateTermsConditionRequest request) {
        TermsCondition termsCondition = TermsCondition.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDate.now())
                .build();

        termsConditionRepository.save(termsCondition);
    }

    @Override
    @Transactional
    public void updateTermsCondition(UpdateTermsConditionRequest request) {
        TermsCondition termsCondition = findById(request.getId());
        termsCondition.setTitle(request.getTitle());
        termsCondition.setContent(request.getContent());

        termsConditionRepository.save(termsCondition);
    }


    @Override
    public void deleteTermsCondition(DeleteTermsCondition request) {
        deleteTermsConditionById(request.getId());
    }


    // 전체 리스트 조회 - Response 형식 - id, 제목
    @Override
    public List<TermsConditionDTO> findAllTermsCondition() {
        List<TermsCondition> termsConditions = findAll();
        return termsConditions.stream()
                .map(tc -> new TermsConditionDTO(tc.getId(), tc.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public TermsCondition findTermsConditionById(Long id) {
        return findById(id);
    }


    private TermsCondition findById(Long id) {
        return termsConditionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 terms id값")
        );
    }

    private void deleteTermsConditionById(Long id) {
        termsConditionRepository.deleteById(id);
    }

    private List<TermsCondition> findAll(){
        return termsConditionRepository.findAll();
    }
}
