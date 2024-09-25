package com.example.domain.member.service.terms;

import com.example.domain.member.entity.TermsCondition;
import com.example.domain.member.repository.TermsConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsConditionServiceImpl implements TermsConditionService{

    private final TermsConditionRepository termsConditionRepository;

    @Override
    public TermsCondition findTermsConditionById(Long id) {
        return termsConditionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 term")
        );
    }

}
