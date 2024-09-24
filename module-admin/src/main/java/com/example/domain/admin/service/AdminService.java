package com.example.domain.admin.service;

import com.example.domain.admin.controller.dto.request.RestoreRequest;
import com.example.domain.admin.controller.dto.response.DeletedFilterResponse;
import com.example.domain.admin.controller.dto.response.FilterResponse;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    public void restoreMember(RestoreRequest request);
    public List<FilterResponse> searchMembers(String field, Object value, String profileField, Object profileValue, LocalDate startDate, LocalDate endDate, String dateField);
    public List<DeletedFilterResponse> searchDeletedMembers(String field, Object value, String profileField, Object profileValue, LocalDate startDate, LocalDate endDate, String dateField);

}
