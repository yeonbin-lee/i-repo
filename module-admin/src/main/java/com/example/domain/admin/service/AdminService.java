package com.example.domain.admin.service;

import com.example.domain.admin.controller.dto.RestoreRequest;
import com.example.domain.member.entity.Member;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    public void restoreMember(RestoreRequest request);
    public List<Member> searchMembers(LocalDate startDate, LocalDate endDate, boolean isBirthDayFilter, Long memberId, String email, String nickname, String gender);
}
