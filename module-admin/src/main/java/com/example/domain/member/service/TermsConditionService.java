package com.example.domain.member.service;

import com.example.domain.admin.controller.dto.request.CreateTermsConditionRequest;
import com.example.domain.admin.controller.dto.request.DeleteTermsCondition;
import com.example.domain.admin.controller.dto.request.UpdateTermsConditionRequest;
import com.example.domain.admin.controller.dto.response.TermsConditionDTO;
import com.example.domain.member.entity.TermsCondition;

import java.util.List;

public interface TermsConditionService {

    public void createTermsCondition(CreateTermsConditionRequest request);
    public void updateTermsCondition(UpdateTermsConditionRequest request);
    public void deleteTermsCondition(DeleteTermsCondition request);
    public TermsCondition findTermsConditionById(Long id);

    public List<TermsConditionDTO> findAllTermsCondition();
}
