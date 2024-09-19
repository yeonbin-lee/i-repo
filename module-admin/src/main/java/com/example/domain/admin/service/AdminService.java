package com.example.domain.admin.service;

import com.example.domain.admin.controller.dto.request.RestoreRequest;
import com.example.domain.admin.controller.dto.response.FilterResponse;
import com.example.domain.member.entity.Member;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    public void restoreMember(RestoreRequest request);
    public List<FilterResponse> searchMembers(String field, Object value, String profileField, Object profileValue, LocalDate startDate, LocalDate endDate, String dateField);
}
